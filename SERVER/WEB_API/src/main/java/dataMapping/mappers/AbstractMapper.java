package dataMapping.mappers;

import dataMapping.Mapper;
import dataMapping.exceptions.ConcurrencyException;
import dataMapping.exceptions.DataMapperException;
import dataMapping.utils.MapperSettings;
import javafx.util.Pair;
import model.DomainObject;
import util.Streamable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static dataMapping.utils.SQLUtils.*;

public abstract class AbstractMapper<T extends DomainObject<K>, K> implements Mapper<T, K> {

    protected final ConcurrentMap<K, T> identityMap = new ConcurrentHashMap<>();
    private final MapperSettings insertSettings;
    private final MapperSettings updateSettings;
    private final MapperSettings deleteSettings;

    public Map<K, T> getIdentityMap() {
        return identityMap;
    }

    public AbstractMapper(MapperSettings insertSettings, MapperSettings updateSettings, MapperSettings deleteSettings){
        this.insertSettings = insertSettings;
        this.updateSettings = updateSettings;
        this.deleteSettings = deleteSettings;
    }

    /**
     * Converts the current row from result set into an object
     * @param rs
     * @return T
     * @throws DataMapperException
     */
    abstract T mapper(ResultSet rs) throws DataMapperException;
    abstract String getSelectQuery();

    protected<R> Streamable<T> findWhere(Pair<String, R>... values){
        StringBuilder queryBuilder = new StringBuilder(getSelectQuery() + " WHERE ");
        for (int i = 0; i < values.length; i++) {
            queryBuilder
                    .append(values[i].getKey())
                    .append(" = ?");
            if(i != values.length -1)
                queryBuilder.append(" AND ");
        }

        return () -> executeSQLQuery(
                queryBuilder.toString(),
                this::mapper,
                statement -> {
                    try{
                        for (int i = 0; i < values.length; i++) {
                            statement.setObject(i, values[i].getValue());
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