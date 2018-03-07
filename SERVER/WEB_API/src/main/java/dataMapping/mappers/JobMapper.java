package dataMapping.mappers;

import dataMapping.exceptions.DataMapperException;
import javafx.util.Pair;
import model.Job;
import util.Streamable;

import java.sql.*;

public class JobMapper extends AbstractMapper<Job, Long> {
    private final String SELECT_QUERY = "SELECT JobID, AccountID, Address, Wage, Description, Schedule, OfferBeginDate, OfferEndDate, OfferType, [version] FROM Job";
    private final String INSERT_QUERY = "INSERT INTO Job (AccountID, Address, Wage, Description, Schedule, OfferBeginDate, OfferEndDate, OfferType, [version]) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private final String UPDATE_QUERY = "UPDATE Job SET Address = ?, Wage = ?, Description = ?, Schedule = ?, OfferBeginDate = ?, OfferEndDate = ?, OfferType = ? WHERE JobID = ? AND [version] = ?";
    private final String DELETE_QUERY = "DELETE FROM Job WHERE JobID = ? AND [version] = ?";

    public Streamable<Job> findForAccount(long accountID){
        return findWhere(new Pair<>("accountId", accountID));
    }

    @Override
    public Job mapper(ResultSet rs) throws DataMapperException {
        try {
            long jobID = rs.getLong("JobID");
            long accountID = rs.getLong("AccountID");
            String address = rs.getString("Address");
            int wage = rs.getInt("Wage");
            String description = rs.getString("Description");
            String schedule = rs.getString("Schedule");
            Date offerBeginDate = rs.getDate("OfferBeginDate");
            Date offerEndDate = rs.getDate("OfferEndDate");
            String offerType = rs.getString("OfferType");
            long version = rs.getLong("Version");

            Job job = Job.load(jobID, accountID, address, wage, description, schedule, offerBeginDate, offerEndDate, offerType, version, null);
            identityMap.put(jobID, job);

            return job;
        } catch (SQLException e) {
            throw new DataMapperException(e.getMessage(), e);
        }
    }

    @Override
    String getSelectQuery() {
        return SELECT_QUERY;
    }

    @Override
    public void insert(Job obj) {
        executeSQLUpdate(
                INSERT_QUERY,
                obj,
                preparedStatement -> {
                    try{
                        preparedStatement.setLong(1, obj.getAccountID());
                        preparedStatement.setString(2, obj.getAddress());
                        preparedStatement.setInt(3, obj.getWage());
                        preparedStatement.setString(4, obj.getDescription());
                        preparedStatement.setString(5, obj.getSchedule());
                        preparedStatement.setDate(6, obj.getOfferBeginDate());
                        preparedStatement.setDate(7, obj.getOfferEndDate());
                        preparedStatement.setString(8, obj.getOfferType());
                        preparedStatement.setLong(9, obj.getVersion());
                    } catch (SQLException e) {
                        throw new DataMapperException(e);
                    }
                }
        );
    }

    @Override
    public void update(Job obj) {
        executeSQLUpdate(
                UPDATE_QUERY,
                obj,
                preparedStatement -> {
                    try{
                        preparedStatement.setString(1, obj.getAddress());
                        preparedStatement.setInt(2, obj.getWage());
                        preparedStatement.setString(3, obj.getDescription());
                        preparedStatement.setString(4, obj.getSchedule());
                        preparedStatement.setDate(5, obj.getOfferBeginDate());
                        preparedStatement.setDate(6, obj.getOfferEndDate());
                        preparedStatement.setString(7, obj.getOfferType());
                        preparedStatement.setLong(8, obj.getVersion());
                    } catch (SQLException e) {
                        throw new DataMapperException(e);
                    }
                }
        );
    }

    @Override
    public void delete(Job obj) {
        executeSQLUpdate(
                DELETE_QUERY,
                obj,
                preparedStatement -> {
                    try{
                        preparedStatement.setLong(1, obj.getIdentityKey());
                        preparedStatement.setLong(2, obj.getVersion());
                    } catch (SQLException e) {
                        throw new DataMapperException(e);
                    }
                }
        );
    }
}
