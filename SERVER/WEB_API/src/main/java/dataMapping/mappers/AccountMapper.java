package dataMapping.mappers;

import model.Account;

public abstract class AccountMapper<T extends Account> extends AbstractMapper<T, Long> {
    private final String SELECT_QUERY = "SELECT AccountID, Email, passwordHash, Rating FROM Account WHERE AccountID = ?";

    @Override
    protected String findByPKStatement() {
        return SELECT_QUERY;
    }
}
