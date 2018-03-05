package dataMapping.mappers;

import dataMapping.exceptions.DataMapperException;
import model.Local;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LocalMapper extends AbstractMapper<Local, String>{
    private final String SELECT_QUERY = "SELECT [Address], Country, District, ZIPCode, [version] FROM [Local] WHERE Address = ?";
    private final String INSERT_QUERY = "INSERT INTO [Local] ([Address], Country, District, ZIPCode) VALUES (?, ?, ?, ?)";
    private final String DELETE_QUERY = "DELETE FROM [Local] WHERE Address = ? AND [version] = ?";

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
        executeSQLUpdate(
                INSERT_QUERY,
                obj,
                false,
                statement -> {
                    try {
                        statement.setString(1, obj.getAddress());
                        statement.setString(2, obj.getCountry());
                        statement.setString(3, obj.getDistrict());
                        statement.setString(4, obj.getZipCode());
                    } catch (SQLException e) {
                        throw new DataMapperException(e);
                    }
                }
        );
    }

    @Override
    public void update(Local obj) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(Local obj) {
        executeSQLUpdate(
                DELETE_QUERY,
                obj,
                true,
                statement -> {
                    try {
                        statement.setString(1, obj.getAddress());
                        statement.setLong(2, obj.getVersion());
                    } catch (SQLException e) {
                        throw new DataMapperException(e);
                    }
                }
        );
    }
}
