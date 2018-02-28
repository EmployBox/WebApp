package dataMapping.mappers;

import dataMapping.exceptions.DataMapperException;
import model.Curriculum;

import java.sql.ResultSet;

public class CurriculumMapper extends AbstractMapper<Curriculum>{
    @Override
    protected String findByPKStatement() {
        return null;
    }

    @Override
    Curriculum mapper(ResultSet rs) throws DataMapperException {
        return null;
    }

    @Override
    public void insert(Curriculum obj) {

    }

    @Override
    public void update(Curriculum obj) {

    }

    @Override
    public void delete(Curriculum obj) {

    }
}
