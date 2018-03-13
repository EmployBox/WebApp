package isel.ps.EmployBox.dataMapping.utils;

import isel.ps.EmployBox.model.DomainObject;

import java.sql.CallableStatement;
import java.sql.Statement;
import java.util.function.BiFunction;

public class MapperSettings<T extends Statement, U extends DomainObject> {

    private final String query;
    private final BiFunction<T, U, U> statementFunction;
    private final boolean isProcedure;

    public MapperSettings(String query, Class<T> tClass, BiFunction<T, U, U> statementFunction) {
        this.query = query;
        this.statementFunction = statementFunction;
        this.isProcedure = tClass.isAssignableFrom(CallableStatement.class);
    }

    public String getQuery() {
        return query;
    }

    public BiFunction<T, U, U> getStatementFunction() {
        return statementFunction;
    }

    public boolean isProcedure() {
        return isProcedure;
    }

    @Override
    public String toString() {
        return "MapperSettings{" +
                "query='" + query + '\'' +
                '}';
    }
}
