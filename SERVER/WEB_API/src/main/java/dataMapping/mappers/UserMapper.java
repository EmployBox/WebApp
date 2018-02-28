package dataMapping.mappers;


import dataMapping.exceptions.DataMapperException;
import model.DomainObject;
import model.User;

import java.sql.ResultSet;

//todo i must finish this
public class UserMapper extends AbstractMapper {
    private final String SELECT_QUERY = "SELECT AccountId, Name, Summary, PhotoUrl FROM User WHERE AccountId = ?";

    private final String UPDATE_QUERY = "UPDATE Job SET Wage = ?, Description = ?, Schedule = ?, OfferBeginDate = ?, OfferEndDate = ?, OfferType = ?, Version = ? WHERE JobID = ? AND Version = ?";
    private final String DELETE_QUERY = "DELETE FROM Job WHERE JobID = ? AND Version = ?";


    @Override
    protected String findByPKStatement() {
        return SELECT_QUERY;
    }

    @Override
    User mapper(ResultSet rs) throws DataMapperException {
        return null;
    }

    @Override
    public void insert(DomainObject obj) {

    }

    @Override
    public void update(DomainObject obj) {

    }

    @Override
    public void delete(DomainObject obj) {

    }
}
