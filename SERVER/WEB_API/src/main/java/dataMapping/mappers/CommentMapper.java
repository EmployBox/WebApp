package dataMapping.mappers;

import dataMapping.exceptions.DataMapperException;
import dataMapping.utils.MapperRegistry;
import dataMapping.utils.MapperSettings;
import javafx.util.Pair;
import model.Comment;
import util.Streamable;

import java.sql.*;

public class CommentMapper extends AbstractMapper {
    private static final String SELECT_QUERY =  "SELECT commentID, accountIdFrom, accountIdTo, mainCommentId, [date], [text], [status] from Comment";
    private static final String INSERT_QUERY =  "INSERT INTO Comment (commentID, accountIdFrom, accountIdTo, mainCommentId, [date], [text], [status]) values (?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_QUERY =  "UPDATE Comment SET [text] = ?, [status] = ? where commentId = ?";
    private static final String DELETE_QUERY =  "DELETE Comment where commentID =  ?";

    public CommentMapper() {
        super(
            new MapperSettings<>(INSERT_QUERY, PreparedStatement.class, CommentMapper::prepareInsertStatement),
            new MapperSettings<>(UPDATE_QUERY, PreparedStatement.class, CommentMapper::prepareUpdateStatement),
            new MapperSettings<>(DELETE_QUERY, PreparedStatement.class, CommentMapper::prepareDeleteStatement)
        );
    }

    @Override
    Comment mapper(ResultSet rs) throws DataMapperException {
        try {
            long commentID = rs.getLong(1);
            long accountIdFrom = rs.getLong(2);
            long accountIdTo = rs.getLong(3);
            long mainCommentId = rs.getLong(4);
            Date date = rs.getDate(5);
            String text = rs.getString(6);
            boolean status = rs.getBoolean(7);
            long version = rs.getLong(8);

            Streamable<Comment> replies = ((CommentMapper) MapperRegistry.getMapper(Comment.class)).findCommentReplies(commentID);

            Comment comment =  Comment.load(commentID,accountIdFrom,accountIdTo, mainCommentId, date, text, status, replies, version);
            return comment;
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }

    public Streamable<Comment> findCommentsForAccount(long accountId) {
        return findWhere(new Pair<>("accountId", accountId));
    }

    public Streamable findCommentReplies(long commentId){
        return findWhere(new Pair<>("mainCommentId",commentId));
    }

    private static void prepareInsertStatement(PreparedStatement statement, Comment comment) {
        try {
            statement.setLong(1, comment.getCommentID());
            statement.setDouble(2, comment.getAccountIdFrom());
            statement.setLong(3, comment.getAccountIdTo());
            statement.setLong(4, comment.getMainCommendID());
            statement.setDate(5, comment.getDate());
            statement.setString(6, comment.getText());
            statement.setBoolean(7, comment.getStatus());
            statement.execute();
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }

    private static void prepareUpdateStatement(PreparedStatement statement, Comment comment) {
        try {
            statement.setString(1, comment.getText());
            statement.setBoolean(2, comment.getStatus());
            statement.execute();
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }

    private static void prepareDeleteStatement(PreparedStatement statement, Comment comment) {
        try {
            statement.setLong(1, comment.getCommentID());
            statement.execute();
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }

    @Override
    String getSelectQuery() {
        return SELECT_QUERY;
    }
}
