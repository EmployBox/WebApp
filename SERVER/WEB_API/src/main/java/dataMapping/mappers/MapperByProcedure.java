package dataMapping.mappers;

import model.DomainObject;

import java.sql.CallableStatement;
import java.util.function.Consumer;

public abstract class MapperByProcedure<T extends DomainObject<K>, K> extends  AbstractMapper<T,K> {
    protected abstract Consumer<CallableStatement> prepareUpdateProcedureArguments(T obj);

    protected void executeSQLProcedure(String call, Consumer<CallableStatement> handleStatement){
        handleSQLStatement(
                call,
                true,
                statement -> handleStatement.accept((CallableStatement) statement)
        );
    }
}
