package dataMapping.mappers;

import dataMapping.exceptions.DataMapperException;
import dataMapping.utils.MapperSettings;
import javafx.util.Pair;
import model.Chat;
import model.DomainObject;
import model.Message;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import util.Streamable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static dataMapping.utils.MapperRegistry.getMapper;

public class ChatMapper extends AbstractMapper<Chat, Long>{
    public ChatMapper() {
        super(
                Chat.class,
                PreparedStatement.class,
                ChatMapper::prepareStatement,
                null,
                ChatMapper::prepareStatement
        );
    }

    public Streamable<Chat> findForAccount(long accountID) {
        return findWhere(new Pair<>("accountIdFirst", accountID));
    }

    @Override
    Chat mapper(ResultSet rs) throws DataMapperException {
        try{
            long chatId = rs.getLong(1);
            long accountIdFirst = rs.getLong(2);
            long accountIdSecond = rs.getLong(3);
            long version = rs.getLong(4);

            Streamable<Message> messages = ((MessageMapper) getMapper(Message.class)).findForChat(chatId);

            Chat chat = Chat.load(chatId, accountIdFirst, accountIdSecond, version, messages);
            identityMap.put(chat.getIdentityKey(), chat);

            return chat;
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }

    private static void prepareStatement(PreparedStatement statement, Chat obj){
        try{
            statement.setLong(1, obj.getChadId());
            statement.setLong(2, obj.getAccountIdFirst());
            statement.setLong(3, obj.getAccountIdSecond());
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }
}
