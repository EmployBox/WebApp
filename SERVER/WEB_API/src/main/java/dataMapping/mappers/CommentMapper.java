package dataMapping.mappers;

import dataMapping.exceptions.DataMapperException;
import dataMapping.utils.MapperSettings;
import model.Comment;
import model.DomainObject;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import util.Streamable;

import java.sql.ResultSet;

public class CommentMapper extends AbstractMapper {
    public CommentMapper(MapperSettings insertSettings, MapperSettings updateSettings, MapperSettings deleteSettings) {
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

    public Streamable<Comment> findCommentsForAccount(long accountID) {
        throw new NotImplementedException();
    }
}
