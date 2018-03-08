package dataMapping.mappers;

import dataMapping.exceptions.DataMapperException;
import dataMapping.utils.MapperSettings;
import model.Chat;
import model.DomainObject;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import util.Streamable;

import java.sql.ResultSet;

public class ChatMapper extends AbstractMapper{
    public ChatMapper(MapperSettings insertSettings, MapperSettings updateSettings, MapperSettings deleteSettings) {
        super(insertSettings, updateSettings, deleteSettings);
    }

    @Override
    DomainObject mapper(ResultSet rs) throws DataMapperException {
        return null;
    }

    @Override
    String getSelectQuery() {
        return null;
    }

    public Streamable<Chat> findChatsForAccount(long accountID) {
        throw new NotImplementedException();
    }
}
