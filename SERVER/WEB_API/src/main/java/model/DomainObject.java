package model;

import dataMapper.UnitOfWork;

public abstract class DomainObject {

    /**
     * The key to find the object on the Identity Map
     */
    private final String identityKey;
    private long version;

    public DomainObject(String identityKey, long version) {
        this.identityKey = identityKey;
        this.version = version;
    }

    public String getIdentityKey() {
        return identityKey;
    }

    public long getVersion() {
        return version;
    }

    public long updateVersion(){
        return ++version;
    }

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
