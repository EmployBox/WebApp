package model;

import dataMapper.UnitOfWork;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public abstract class DomainObject {

    /**
     * Always called when creating new object
     */
    protected void markNew() {
        UnitOfWork.getCurrent().registerNew(this);
    }

    /**
     * Always called when reading an object from DB
     */
    protected void markClean() {
        UnitOfWork.getCurrent().registerClean(this);
    }

    /**
     * To be always called before making any changes to the object and calling markDirty()
     */
    protected void markToBeDirty(){
        UnitOfWork.getCurrent().registerClone(this);
    }

    /**
     * Always called when altering an object
     */
    protected void markDirty() {
        UnitOfWork.getCurrent().registerDirty(this);
    }

    /**
     * Always called when removing an object
     */
    protected void markRemoved() {
        UnitOfWork.getCurrent().registerRemoved(this);
    }
}
