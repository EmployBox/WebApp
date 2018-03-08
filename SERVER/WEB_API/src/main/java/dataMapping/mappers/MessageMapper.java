package dataMapping.mappers;

import dataMapping.exceptions.DataMapperException;
import model.Message;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MessageMapper extends AbstractMapper<Message,String> {

    //TODO
    public MessageMapper() {
        super(
            null,
            null,
            null
        );
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
}
