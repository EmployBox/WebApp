package isel.ps.EmployBox.dataMapping.mappers;


import isel.ps.EmployBox.model.Account;

import java.sql.CallableStatement;
import java.sql.Statement;
import java.util.function.BiConsumer;

public abstract class AccountMapper<T extends Account> extends AbstractMapper<T, Long> {

    public AccountMapper(Class<T> type, BiConsumer<CallableStatement, T> prepareInsertFunction,
                                              BiConsumer<CallableStatement, T> prepareUpdateFunction,
                                              BiConsumer<CallableStatement, T> prepareDeleteFunction) {
        super(type, CallableStatement.class, prepareInsertFunction, prepareUpdateFunction, prepareDeleteFunction);
    }
}
