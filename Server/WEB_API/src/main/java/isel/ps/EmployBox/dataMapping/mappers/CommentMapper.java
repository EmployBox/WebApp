package isel.ps.EmployBox.dataMapping.mappers;

import isel.ps.EmployBox.dataMapping.exceptions.DataMapperException;
import isel.ps.EmployBox.dataMapping.utils.MapperRegistry;
import isel.ps.EmployBox.dataMapping.utils.MapperSettings;
import javafx.util.Pair;
import isel.ps.EmployBox.model.Comment;
import isel.ps.EmployBox.util.Streamable;

import java.sql.*;

public class CommentMapper extends AbstractMapper<Comment, Long> {
    public CommentMapper() {
        super(
                Comment.class,
                PreparedStatement.class,
                CommentMapper::prepareInsertStatement,
                CommentMapper::prepareUpdateStatement,
                CommentMapper::prepareDeleteStatement
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

    private static Comment prepareInsertStatement(PreparedStatement statement, Comment comment) {
        try {
            statement.setDouble(1, comment.getAccountIdFrom());
            statement.setLong(2, comment.getAccountIdTo());
            statement.setLong(3, comment.getMainCommendID());
            statement.setDate(4, comment.getDate());
            statement.setString(5, comment.getText());
            statement.setBoolean(6, comment.getStatus());
            executeUpdate(statement);

            long version = getVersion(statement);
            long commentId = getGeneratedKey(statement);

            return new Comment(commentId, comment.getAccountIdFrom(), comment.getAccountIdTo(), comment.getMainCommendID(), comment.getDate(), comment.getText(), comment.getStatus(),
                    comment.getReplies(), version);
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }

    private static Comment prepareUpdateStatement(PreparedStatement statement, Comment comment) {
        try {
            statement.setString(1, comment.getText());
            statement.setBoolean(2, comment.getStatus());
            executeUpdate(statement);

            long version = getVersion(statement);

            return new Comment(comment.getCommentID(), comment.getAccountIdFrom(), comment.getAccountIdTo(), comment.getMainCommendID(), comment.getDate(), comment.getText(), comment.getStatus(),
                    comment.getReplies(), version);
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }

    private static Comment prepareDeleteStatement(PreparedStatement statement, Comment comment) {
        try {
            statement.setLong(1, comment.getCommentID());
            statement.executeUpdate();
            return null;
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }
}
