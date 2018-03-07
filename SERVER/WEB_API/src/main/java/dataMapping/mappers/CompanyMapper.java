package dataMapping.mappers;

import dataMapping.exceptions.DataMapperException;
import dataMapping.utils.MapperRegistry;
import dataMapping.utils.MapperSettings;
import model.Company;
import model.Job;
import util.Streamable;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class CompanyMapper extends AccountMapper<Company> {

    public CompanyMapper() {
        super(
            new MapperSettings<>("{call AddCompany(?, ?, ?, ?, ?, ?, ?, ?, ?, ,? ,?)}", CallableStatement.class, CompanyMapper::prepareUpdateProcedureArguments),
            new MapperSettings<>("{call UpdateCompany (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}", CallableStatement.class, CompanyMapper::prepareUpdateProcedureArguments),
            new MapperSettings<>("{call DeleteCompany (?, ?)}", CallableStatement.class, CompanyMapper::prepareDeleteProcedure)
        );
    }

    @Override
    Company mapper(ResultSet rs) throws DataMapperException {
        try {
            long accountId = rs.getLong("accountId");
            String email = rs.getString("Email");
            Double rating = rs.getDouble("Rating");
            String name = rs.getString("name");
            short yearFounded = rs.getShort("yearFounded");
            String specialization = rs.getString("Specialization");
            String webPageUrl = rs.getString("WebPageUrl");
            String logoUrl = rs.getString("LogoUrl");
            String description = rs.getString("[description]");
            long version = rs.getLong("version");

            Streamable<Job> offeredJobs = ((JobMapper) MapperRegistry.getMapper(Job.class)).findForAccount(accountId);

            Company company = new Company(accountId, email,null, rating,version, name, specialization, yearFounded,logoUrl, webPageUrl, description, offeredJobs);
            identityMap.put(accountId, company);
        }catch (SQLException e){
            throw new DataMapperException(e.getMessage(), e);
        }
        return null;
    }

    @Override
    String getSelectQuery() {
        return null;
    }

    private static void prepareUpdateProcedureArguments(CallableStatement cs, Company obj) {
        try {
            cs.setString(1, obj.getEmail());
            cs.setString(2,obj.getPassword());
            cs.setDouble(3, obj.getRating());
            cs.setString(4, obj.getName());
            cs.setString(5, obj.getSpecialization());
            cs.setShort(6, obj.getYearFounded());
            cs.setString(7, obj.getLogoUrl());
            cs.setString(8, obj.getWebPageUrl());
            cs.setString(9, obj.getDescription());
            cs.registerOutParameter(10, Types.BIGINT);
            cs.registerOutParameter(11, Types.NVARCHAR);
            cs.execute();
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }

    private static void prepareDeleteProcedure(CallableStatement callableStatement, Company obj){
        try {
            callableStatement.setString(1, obj.getEmail());
            callableStatement.registerOutParameter(2, Types.NVARCHAR);
            callableStatement.execute();
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }
}
