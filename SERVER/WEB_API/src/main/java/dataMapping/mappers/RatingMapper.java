package dataMapping.mappers;

import dataMapping.exceptions.DataMapperException;
import dataMapping.utils.MapperSettings;
import model.DomainObject;
import model.Rating;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import util.Streamable;

import java.sql.ResultSet;

public class RatingMapper extends AbstractMapper {
    public RatingMapper(MapperSettings insertSettings, MapperSettings updateSettings, MapperSettings deleteSettings) {
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

    public Streamable<Rating> findRatingsForAccount(long accountID) {
        throw new NotImplementedException();
    }

    public Streamable<Rating> findModeratedRatingsForModerator(long moderatorID) {
        throw new NotImplementedException();
    }
}
