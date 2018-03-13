package isel.ps.EmployBox.dataMapping.mappers;

import isel.ps.EmployBox.dataMapping.exceptions.DataMapperException;
import isel.ps.EmployBox.dataMapping.utils.MapperSettings;
import isel.ps.EmployBox.model.Local;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LocalMapper extends AbstractMapper<Local, String>{
    public LocalMapper() {
        super(
                Local.class,
                PreparedStatement.class,
                LocalMapper::prepareInsertStatement,
                null,
                LocalMapper::prepareDeleteStatement
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