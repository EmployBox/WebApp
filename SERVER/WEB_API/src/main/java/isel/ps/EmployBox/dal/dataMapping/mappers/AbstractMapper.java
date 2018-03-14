package isel.ps.EmployBox.dal.dataMapping.mappers;

import isel.ps.EmployBox.dal.dataMapping.DataBaseConnectivity;
import isel.ps.EmployBox.dal.dataMapping.utils.MapperSettings;
import isel.ps.EmployBox.dal.dataMapping.utils.SQLUtils;
import isel.ps.EmployBox.dal.dataMapping.Mapper;
import isel.ps.EmployBox.dal.dataMapping.exceptions.ConcurrencyException;
import isel.ps.EmployBox.dal.dataMapping.exceptions.DataMapperException;
import isel.ps.EmployBox.dal.util.ReflectionUtils;
import isel.ps.EmployBox.dal.util.Streamable;
import isel.ps.EmployBox.dal.domainModel.DomainObject;
import isel.ps.EmployBox.dal.domainModel.ID;
import javafx.util.Pair;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.Arrays;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class AbstractMapper<T extends DomainObject<K>, K> implements Mapper<T, K> {

    protected final ConcurrentMap<K, T> identityMap = new ConcurrentHashMap<>();

    protected final String SELECT_QUERY;
    private final MapperSettings<? extends Statement, T, K> insertSettings;
    private final MapperSettings<? extends Statement, T, K> updateSettings;
    private final MapperSettings<? extends Statement, T, K> deleteSettings;
    private final DataBaseConnectivity<T, K> dbc;

    public <R extends Statement> AbstractMapper(
            Class<T> type,
            Class<R> statementType,
            BiFunction<R, T, T> prepareInsertFunction,
            BiFunction<R, T, T> prepareUpdateFunction,
            BiFunction<R, T, T> prepareDeleteFunction
    ) {
        this.dbc = new SQLUtils<>();

        Field[] fields = ReflectionUtils.allFieldsFor(type).filter(f ->
                (f.getType().isPrimitive() ||
                f.getType().isAssignableFrom(String.class) ||
                f.getType().isAssignableFrom(Date.class)) &&
                        !(f.getName().equals("identityKey") || f.getName().equals("defaultKey")))
                .toArray(Field[]::new);

        this.SELECT_QUERY = Arrays.stream(fields)
                .map(Field::getName)
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
                    f -> {sjD.add("?"); sjI.add("?"); sjU.add("?");},
                    f -> {sjI.add("?"); sjU.add("?");});

            sI = sjI.toString();
            sU = sjU.toString();
            sD = sjD.toString();
        }
        else {
            StringJoiner sjI = new StringJoiner(",", "insert into ["+type.getSimpleName()+"] (", ") output INSERTED.[version]");
            StringJoiner sjU = new StringJoiner(",", "update ["+type.getSimpleName()+"] set ","output INSERTED.[version] where ");
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

    @Override
    public CompletableFuture<T> getById(K id) {
        return null;
    }

    @Override
    public CompletableFuture<List<T>> getAll() {
        dbc.executeSQLQuery(SELECT_QUERY, this::mapper, s -> {});

        return null;
    }

    protected<R> Streamable<T> findWhere(Pair<String, R>... values){
        String query = Arrays.stream(values)
                .map(p -> p.getKey() + " = ? ")
                .collect(Collectors.joining(" AND ", SELECT_QUERY + " WHERE ", ""));

        return dbc.executeSQLQuery(
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
                })::join;
    }

    private boolean tryReplace(T obj, long previousVersion, long timeout){
        long target = System.currentTimeMillis() +  timeout;
        long remaining = target - System.currentTimeMillis();

        while(remaining >= target){
            T observedObj = identityMap.get(obj.getIdentityKey());
            if(previousVersion < obj.getVersion()) {
                if(identityMap.replace(obj.getIdentityKey(), observedObj, obj))
                    return true;
            }
            remaining = target - System.currentTimeMillis();
            Thread.yield();
        }
        return false;
    }

    private<R extends Statement> CompletableFuture<T> executeStatement(MapperSettings<R, T, K> mapperSettings, T obj){
        BiFunction<String, Function<? super Statement, T>, CompletableFuture<T>> execute = mapperSettings.isProcedure() ? dbc::executeSQLProcedure : dbc::executeSQLUpdate;
        return execute.apply(mapperSettings.getQuery(), statement -> mapperSettings.getStatementFunction().apply((R)statement, obj));
    }

    protected static void executeUpdate(PreparedStatement statement) throws SQLException {
        int rowCount = statement.executeUpdate();
        if (rowCount == 0) throw new ConcurrencyException("Concurrency problem found");
    }

    protected static long getVersion(PreparedStatement statement) throws SQLException {
        long version;
        try (ResultSet inserted = statement.getResultSet()) {
            if (inserted.next()){
                version = inserted.getLong(1);
            }
            else throw new DataMapperException("Error inserting new entry");
        }
        return version;
    }

    protected static long getGeneratedKey(PreparedStatement preparedStatement) throws SQLException {
        long jobId;
        try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
            if (generatedKeys.next()){
                jobId = generatedKeys.getLong(1);
            }
            else throw new DataMapperException("Error inserting new entry");
        }
        return jobId;
    }

    @Override
    public void insert(T obj) {
        executeStatement(insertSettings, obj).thenAccept(o -> identityMap.put(o.getIdentityKey(), o));
    }

    @Override
    public void update(T obj) {
        long previousVersion = obj.getVersion();
        executeStatement(updateSettings, obj)
                .thenApply(o -> tryReplace(o, previousVersion, 5000))
                .thenAccept(b -> {if(!b) throw new ConcurrencyException("Concurrency problem found, could not update IdentityMap");});
    }

    @Override
    public void delete(T obj) {
        executeStatement(deleteSettings, obj).thenAccept(o -> identityMap.remove(obj.getIdentityKey()));
    }
}