package isel.ps.EmployBox.dataMapping.mappers;


import isel.ps.EmployBox.model.Account;

import java.sql.CallableStatement;
import java.sql.Statement;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public abstract class AccountMapper<T extends Account> extends AbstractMapper<T, Long> {

    public AccountMapper(
            Class<T> type, BiFunction<CallableStatement, T, T> prepareInsertFunction,
            BiFunction<CallableStatement, T, T> prepareUpdateFunction,
            BiFunction<CallableStatement, T, T> prepareDeleteFunction
    ) {
        super(type, CallableStatement.class, prepareInsertFunction, prepareUpdateFunction, prepareDeleteFunction);
    }
}
