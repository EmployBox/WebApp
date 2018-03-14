package isel.ps.EmployBox.dal.dataMapping.mappers;

import isel.ps.EmployBox.dal.dataMapping.exceptions.DataMapperException;
import javafx.util.Pair;
import isel.ps.EmployBox.dal.domainModel.Message;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MessageMapper extends AbstractMapper<Message,String> {
    public MessageMapper() {
        super(
                Message.class,
                PreparedStatement.class,
                MessageMapper::prepareInsertStatement,
                null,
                MessageMapper::prepareDeleteStatement
        );
    }

    public CompletableFuture<List<Message>> findForChat(long chatId){
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

    private static Message prepareInsertStatement(PreparedStatement statement, Message obj){
        try{
            statement.setLong(1, obj.getMessageId());
            statement.setLong(2, obj.getChadId());
            statement.setString(3, obj.getText());
            statement.setDate(4, obj.getDate());
            executeUpdate(statement);

            long version = getVersion(statement);

            return new Message(obj.getMessageId(), obj.getChadId(), obj.getText(), obj.getDate(), version);
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }

    private static Message prepareDeleteStatement(PreparedStatement statement, Message obj){
        try{
            statement.setLong(1, obj.getMessageId());
            statement.setLong(2, obj.getChadId());
            statement.setLong(3, obj.getVersion());
            executeUpdate(statement);
            return null;
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }
}
