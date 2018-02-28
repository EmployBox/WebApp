package dataMapping.mappers;

import model.Account;

public abstract class AccountMapper<T extends Account> extends AbstractMapper<T> {
    private final String SELECT_QUERY = "SELECT AccountID, Email, passwordHash, Rating FROM Account WHERE AccountID = ?";
    private final String INSERT_QUERY = "INSERT INTO Account (Email, Password, Rating, Version) VALUES (?, ?, ?, ?)";
    private final String UPDATE_QUERY = "UPDATE Account SET Password = ?, Rating = ?, Version = ? WHERE AccountID = ? AND Version = ?";
    private final String DELETE_QUERY = "DELETE FROM Account WHERE AccountID = ? AND Version = ?";


    @Override
    protected String findByPKStatement() {
        return SELECT_QUERY;
    }


    public abstract void insert(T obj);

    public abstract void update(T obj);


    public abstract void delete(T obj);
}
