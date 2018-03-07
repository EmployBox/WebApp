package dataMapping.mappers;

import dataMapping.exceptions.DataMapperException;
import dataMapping.utils.MapperSettings;
import model.Local;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LocalMapper extends AbstractMapper<Local, String>{
    private static final String SELECT_QUERY = "SELECT [Address], Country, District, ZIPCode, [version] FROM [Local]";
    private static final String INSERT_QUERY = "INSERT INTO [Local] ([Address], Country, District, ZIPCode) VALUES (?, ?, ?, ?)";
    private static final String DELETE_QUERY = "DELETE FROM [Local] WHERE Address = ? AND [version] = ?";

    public LocalMapper() {
        super(
            new MapperSettings<>(INSERT_QUERY, PreparedStatement.class, LocalMapper::prepareInsertStatement),
            null,
            new MapperSettings<>(DELETE_QUERY, PreparedStatement.class, LocalMapper::prepareDeleteStatement)
        );
    }

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
    String getSelectQuery() {
        return SELECT_QUERY;
    }


    private static void prepareInsertStatement(PreparedStatement statement, Local obj) {
        try {
            statement.setString(1, obj.getAddress());
            statement.setString(2, obj.getCountry());
            statement.setString(3, obj.getDistrict());
            statement.setString(4, obj.getZipCode());
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }

    private static void prepareDeleteStatement(PreparedStatement statement, Local obj) {
        try {
            statement.setString(1, obj.getAddress());
            statement.setLong(2, obj.getVersion());
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }
}
