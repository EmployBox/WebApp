package isel.ps.EmployBox.dataMapping.mappers;

import isel.ps.EmployBox.dataMapping.Mapper;
import isel.ps.EmployBox.dataMapping.exceptions.ConcurrencyException;
import isel.ps.EmployBox.dataMapping.exceptions.DataMapperException;
import isel.ps.EmployBox.dataMapping.utils.MapperSettings;
import isel.ps.EmployBox.model.DomainObject;
import isel.ps.EmployBox.model.ID;
import isel.ps.EmployBox.util.Streamable;
import javafx.util.Pair;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.Arrays;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static isel.ps.EmployBox.dataMapping.utils.SQLUtils.*;
import static isel.ps.EmployBox.util.ReflectionUtils.allFieldsFor;

public abstract class AbstractMapper<T extends DomainObject<K>, K> implements Mapper<T, K> {

    protected final ConcurrentMap<K, T> identityMap = new ConcurrentHashMap<>();

    protected final String SELECT_QUERY;
    private final MapperSettings insertSettings;
    private final MapperSettings updateSettings;
    private final MapperSettings deleteSettings;

    public <R extends Statement> AbstractMapper(
            Class<T> type,
            Class<R> statementType,
            BiConsumer<R, T> prepareInsertFunction,
            BiConsumer<R, T> prepareUpdateFunction,
            BiConsumer<R, T> prepareDeleteFunction
    ) {

        Field[] fields = allFieldsFor(type).filter(f ->
                (f.getType().isPrimitive() ||
                f.getType().isAssignableFrom(String.class) ||
                f.getType().isAssignableFrom(Date.class)) &&
                        !(f.getName().equals("identityKey") || f.getName().equals("defaultKey")))
                .toArray(Field[]::new);

        this.SELECT_QUERY = Arrays.stream(fields)
                .map(f -> f.getName().equals("version") || f.getName().equals("date") ? "[" + f.getName()+ "]" : f.getName())
                .collect(Collectors.joining(", ", "select ", " from ["+type.getSimpleName()+"]"));

        String sI;
        String sU;
        String sD;

        //System.out.println(statementType +" : " + (statementType == CallableStatement.class));

        if(statementType == CallableStatement.class){
            StringJoiner sjI = new StringJoiner(",","{call Add"+type.getSimpleName()+"(", ")}");
            StringJoiner sjU = new StringJoiner(",","{call Update"+type.getSimpleName()+"(", ")}");
            //StringJoiner sjD = new StringJoiner(",","{call Delete"+type.getSimpleName()+"(", ")}");
            StringJoiner sjD = new StringJoiner(",","{call DeleteAccount(", ")}");

            queryBuilder(fields,
                    f -> sjI.add("?"),
                    f -> {sjD.add("?"); sjI.add("?");},
                    f -> {sjI.add("?"); sjU.add("?");});

            sI = sjI.toString();
            sU = sjU.toString();
            sD = sjD.toString();
        }
        else {
            StringJoiner sjI = new StringJoiner(",", "insert into ["+type.getSimpleName()+"] (", ")");
            StringJoiner sjU = new StringJoiner(",", "update ["+type.getSimpleName()+"] set "," where ");
            StringJoiner sjD = new StringJoiner("and", "delete from ["+type.getSimpleName()+"] where ", "");

            StringJoiner sjUAux = new StringJoiner(" and ");
            StringJoiner sjIAux = new StringJoiner(" , ", " values(", ")");

            Function<Field,String> func = f -> f.getName().equals("version") || f.getName().equals("date") ? "[" + f.getName()+ "]" : f.getName();

            queryBuilder(
                    fields,
                    f -> {sjI.add(func.apply(f)); sjIAux.add(" ? ");},
                    f -> {sjD.add(func.apply(f)); sjUAux.add(func.apply(f) +" = ? ");},
                    f -> {sjI.add(func.apply(f)); sjU.add(func.apply(f) + " = ? "); sjIAux.add(" ? ");}
            );

            sU = sjU.toString() + sjUAux.toString();
            sI = sjI.toString() + sjIAux.toString();
            sD = sjD.toString();
        }

        insertSettings = new MapperSettings<>(sI, statementType, prepareInsertFunction);
        updateSettings = new MapperSettings<>(sU, statementType, prepareUpdateFunction);
        deleteSettings = new MapperSettings<>(sD, statementType, prepareDeleteFunction);
    }

    private static void queryBuilder(Field[] fieds, Consumer<Field> first, Consumer<Field> second, Consumer<Field> third){
        for(Field f : fieds){
            if(f.isAnnotationPresent(ID.class)){
                if(!f.getAnnotation(ID.class).isIdentity()) {
                    first.accept(f);
                }
                second.accept(f);
            }
            else
                third.accept(f);
        }
    }

    public Map<K, T> getIdentityMap() {
        return identityMap;
    }

    /**
     * Converts the current row from result set into an object
     * @param rs
     * @return T
     * @throws DataMapperException
     */
    abstract T mapper(ResultSet rs) throws DataMapperException;
    //abstract String getSelectQuery();

    protected<R> Streamable<T> findWhere(Pair<String, R>... values){
//        StringBuilder queryBuilder = new StringBuilder(getSelectQuery() + " WHERE ");
//        for (int i = 0; i < values.length; i++) {
//            queryBuilder
//                    .append(values[i].getKey())
//                    .append(" = ?");
//            if(i != values.length -1)
//                queryBuilder.append(" AND ");
//        }

        String query = Arrays.stream(values)
                .map(p -> p.getKey() + " = ? ")
                .collect(Collectors.joining(" AND ", SELECT_QUERY + " WHERE ", ""));

        return () -> executeSQLQuery(
                query,
                this::mapper,
                statement -> {
                    try{
                        for (int i = 0; i < values.length; i++) {
                            statement.setObject(i+1, values[i].getValue());
                        }
                    } catch (SQLException e) {
                        throw new DataMapperException(e);
                    }
                }
        );
    }

    private boolean tryReplace(T obj, long timeout){
        long target = System.currentTimeMillis() +  timeout;
        long remaining = target - System.currentTimeMillis();

        while(remaining >= target){
            T observedObj = identityMap.get(obj.getIdentityKey());
            if(observedObj.getVersion() + 1 == obj.getVersion()) {
                if(identityMap.replace(obj.getIdentityKey(), observedObj, obj))
                    return true;
            }
            remaining = target - System.currentTimeMillis();
            Thread.yield();
        }
        return false;
    }

    private void handleCRUD(MapperSettings mapperSettings, T obj){
        if(mapperSettings.isProcedure())
            executeSQLProcedure(mapperSettings.getQuery(), obj, mapperSettings.getStatementConsumer());
        else
            executeSQLUpdate(mapperSettings.getQuery(), obj, mapperSettings.getStatementConsumer());
    }

    //TODO Auto generated keys, obj's key will be null. Update version as well
    @Override
    public void insert(T obj) {
        handleCRUD(insertSettings, obj);

        identityMap.put(obj.getIdentityKey(), obj);
    }

    @Override
    public void update(T obj) {
        handleCRUD(updateSettings, obj);

        if(!tryReplace(obj, 5000)) throw new ConcurrencyException("Concurrency problem found, could not update IdentityMap");
    }

    @Override
    public void delete(T obj) {
        handleCRUD(deleteSettings, obj);

        identityMap.remove(obj.getIdentityKey());
    }
}