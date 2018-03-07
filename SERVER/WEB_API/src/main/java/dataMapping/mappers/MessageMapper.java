package dataMapping.mappers;

import dataMapping.exceptions.DataMapperException;
import dataMapping.utils.MapperSettings;
import javafx.util.Pair;
import model.Message;
import util.Streamable;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MessageMapper extends AbstractMapper<Message,String> {
    private static final String SELECT_QUERY = "SELECT messageId, chatId, [text], [date], [version] FROM [MESSAGE]";
    private static final String INSERT_QUERY = "INSERT INTO [MESSAGE] (messageId, chatId, [text], [date]) VALUES (?, ?, ?, ?)";
    private static final String DELETE_QUERY = "DELETE FROM [MESSAGE] WHERE messageId = ? AND chatId = ? AND [version] = ?";

    public MessageMapper() {
        super(
            new MapperSettings<>(INSERT_QUERY, PreparedStatement.class, MessageMapper::prepareInsertStatement),
            null,
            new MapperSettings<>(DELETE_QUERY, PreparedStatement.class, MessageMapper::prepareDeleteStatement)
        );
    }

    public Streamable<Message> findForChat(long chatId){
        return findWhere(new Pair<>("chatId", chatId));
    }

    @Override
    Message mapper(ResultSet rs) throws DataMapperException {
        try {
            long messageId = rs.getLong("messageId");
            long chatId = rs.getLong("chatId");
            String text = rs.getString("text");
            Date date = rs.getDate("date");
            long version = rs.getLong("[version]");

            Message message = Message.load(messageId, chatId, text, date, version);
            identityMap.put(String.format("%d %d",chatId,messageId),message);
            return message;
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }

    @Override
    String getSelectQuery() {
        return SELECT_QUERY;
    }

    private static void prepareInsertStatement(PreparedStatement statement, Message obj){
        try{
            statement.setLong(1, obj.getMessageId());
            statement.setLong(2, obj.getChadId());
            statement.setString(3, obj.getText());
            statement.setDate(4, obj.getDate());
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }

    private static void prepareDeleteStatement(PreparedStatement statement, Message obj){
        try{
            statement.setLong(1, obj.getMessageId());
            statement.setLong(2, obj.getChadId());
            statement.setLong(3, obj.getVersion());
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }
}
