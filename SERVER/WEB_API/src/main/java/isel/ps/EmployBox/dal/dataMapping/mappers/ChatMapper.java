package isel.ps.EmployBox.dal.dataMapping.mappers;

import isel.ps.EmployBox.dal.dataMapping.utils.MapperRegistry;
import isel.ps.EmployBox.dal.dataMapping.exceptions.DataMapperException;
import javafx.util.Pair;
import isel.ps.EmployBox.dal.domainModel.Chat;
import isel.ps.EmployBox.dal.domainModel.Message;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ChatMapper extends AbstractMapper<Chat, Long>{
    public ChatMapper() {
        super(
                Chat.class,
                PreparedStatement.class,
                ChatMapper::prepareInsertStatement,
                null,
                ChatMapper::prepareDeleteStatement
        );
    }

    public CompletableFuture<List<Chat>> findForAccount(long accountID) {
        return findWhere(new Pair<>("accountIdFirst", accountID));
    }

    @Override
    Chat mapper(ResultSet rs) throws DataMapperException {
        try{
            long chatId = rs.getLong(1);
            long accountIdFirst = rs.getLong(2);
            long accountIdSecond = rs.getLong(3);
            long version = rs.getLong(4);

            Supplier<List<Message>> messages = ((MessageMapper) MapperRegistry.getMapper(Message.class)).findForChat(chatId)::join;

            Chat chat = Chat.load(chatId, accountIdFirst, accountIdSecond, version, messages);
            identityMap.put(chat.getIdentityKey(), chat);

            return chat;
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }

    private static Chat prepareInsertStatement(PreparedStatement statement, Chat obj){
        try{
            statement.setLong(1, obj.getAccountIdFirst());
            statement.setLong(2, obj.getAccountIdSecond());
            executeUpdate(statement);

            long version = getVersion(statement);
            long chatId = getGeneratedKey(statement);

            return new Chat(chatId, obj.getAccountIdFirst(), obj.getAccountIdSecond(), version, ()->obj.getMessages().collect(Collectors.toList()));
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }

    private static Chat prepareDeleteStatement(PreparedStatement statement, Chat obj){
        try{
            statement.setLong(1, obj.getChadId());
            executeUpdate(statement);
            return null;
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }
}
