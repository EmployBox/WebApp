package dataMapping.mappers;

import dataMapping.exceptions.DataMapperException;
import dataMapping.utils.MapperRegistry;
import model.Company;
import model.Job;
import util.Streamable;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.function.Consumer;

public class CompanyMapper extends MapperByProcedure<Company,Long> {

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

    @Override
    protected Consumer<CallableStatement> prepareUpdateProcedureArguments(Company obj) {
        return cs -> {
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
                identityMap.put(obj.getIdentityKey(), obj);
            } catch (SQLException e) {
                throw new DataMapperException(e);
            }
        };
    }

    @Override
    public void insert(Company obj) {
        executeSQLProcedure(
                "{call AddCompany(?, ?, ?, ?, ?, ?, ?, ?, ?, ,? ,?)}",
                prepareUpdateProcedureArguments(obj)
        );
    }

    @Override
    public void update(Company obj) {
        executeSQLProcedure(
                "{call UpdateCompany (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}",
                prepareUpdateProcedureArguments(obj)
        );
    }

    @Override
    public void delete(Company obj) {
        executeSQLProcedure(
        "{call DeleteCompany(?, ?)}",
            callableStatement -> {
                try {
                    callableStatement.setString(1, obj.getEmail());
                    callableStatement.registerOutParameter(2, Types.NVARCHAR);
                    callableStatement.execute();
                    identityMap.remove(obj.getIdentityKey());
                } catch (SQLException e) {
                    throw new DataMapperException(e);
                }
            }
        );
    }
}
