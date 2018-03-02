package model;

import dataMapping.utils.UnitOfWork;

public abstract class DomainObject<K> {

    /**
     * The key to find the object on the Identity Map
     */
    private K identityKey;
    private final K defaultKey;
    private final long version;

    public DomainObject(K identityKey, K defaultKey, long version) {
        this.identityKey = identityKey;
        this.defaultKey = defaultKey;
        this.version = version;
    }

    public K getIdentityKey() {
        return identityKey;
    }

    public void setIdentityKey(K key){
        if(identityKey == defaultKey)
            identityKey = key;
    }

    public long getVersion() {
        return version;
    }

    public K getDefaultKey() {
        return defaultKey;
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
