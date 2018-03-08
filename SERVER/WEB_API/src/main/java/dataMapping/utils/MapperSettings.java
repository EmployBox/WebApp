package dataMapping.utils;

import model.DomainObject;

import java.sql.CallableStatement;
import java.sql.Statement;
import java.util.function.BiConsumer;

public class MapperSettings<T extends Statement, U extends DomainObject> {

    private final String query;
    private final BiConsumer<T, U> statementConsumer;
    private final boolean isProcedure;

    public MapperSettings(String query, Class<T> tClass, BiConsumer<T, U> statementConsumer) {
        this.query = query;
        this.statementConsumer = statementConsumer;
        this.isProcedure = tClass.isAssignableFrom(CallableStatement.class);
    }

    public String getQuery() {
        return query;
    }

    public BiConsumer<T, U> getStatementConsumer() {
        return statementConsumer;
    }

    public boolean isProcedure() {
        return isProcedure;
    }
}
