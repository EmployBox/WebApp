package dataMapping.mappers;

import dataMapping.exceptions.DataMapperException;
import model.Local;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LocalMapper extends AbstractMapper<Local, String>{
    public LocalMapper() {
        super(Local.class);
    }
//    private final String SELECT_QUERY = "SELECT Address, Country, District, ZIPCode FROM Local WHERE Address = ?";

    @Override
    Local mapper(ResultSet rs) throws DataMapperException {
        try{
            String address = rs.getString("Address");
            String country = rs.getString("Country");
            String district = rs.getString("District");
            String zipCode = rs.getString("ZIPCode");
            long version = rs.getLong("[version]");

            Local local = Local.load(address, country, district, zipCode, version);
            identityMap.put(local.getIdentityKey(), local);

            return local;
        } catch (SQLException e) {
            throw new DataMapperException(e.getMessage(), e);
        }
    }

    @Override
    public void insert(Local obj) {
        //TODO
    }

    @Override
    public void update(Local obj) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(Local obj) {
        //TODO
    }
}
