package dataMapping.utils;

import dataMapping.exceptions.ConcurrencyException;
import model.DomainObject;

import java.util.ArrayList;
import java.util.List;

public class UnitOfWork {
    /**
     * Private for each transaction
     */
    private List<DomainObject> newObjects = new ArrayList<>();
    private List<DomainObject> clonedObjects = new ArrayList<>();
    private List<DomainObject> dirtyObjects = new ArrayList<>();
    private List<DomainObject> removedObjects = new ArrayList<>();

    /**
     * Adds the obj to the newObjects List and to the IdentityMap
     * @param obj
     */
    public void registerNew(DomainObject obj) {
        assert obj.getIdentityKey() != null;
        assert !dirtyObjects.contains(obj);
        assert !removedObjects.contains(obj);
        assert !newObjects.contains(obj);
        newObjects.add(obj);
    }

    public void registerClone(DomainObject obj) {
        assert obj.getIdentityKey()!= null;
        assert !removedObjects.contains(obj);
        if(!clonedObjects.contains(obj) && !newObjects.contains(obj))
            clonedObjects.add(obj);
    }

    /**
     * Tags the object to be updated on the DB
     * @param obj
     */
    public void registerDirty(DomainObject obj){
        assert obj.getIdentityKey()!= null;
        assert !removedObjects.contains(obj);
        if(!dirtyObjects.contains(obj) && !newObjects.contains(obj))
            dirtyObjects.add(obj);
    }

    /**
     * Removes the obj from newObjects and/or dirtyObjects and from the IdentityMap
     * @param obj
     */
    public void registerRemoved(DomainObject obj){
        assert obj.getIdentityKey()!= null;
        if(newObjects.remove(obj)) return;
        dirtyObjects.remove(obj);
        if(!removedObjects.contains(obj))
            removedObjects.add(obj);
    }

    /**
     * Adds the new obj to the IdentityMap
     * @param obj
     */
    public void registerClean(DomainObject obj){
        assert obj.getIdentityKey()!= null;
        MapperRegistry.getMapper(obj.getClass()).getIdentityMap().put(obj.getIdentityKey(), obj);
    }

    private static ThreadLocal<UnitOfWork> current = new ThreadLocal<>();

    /**
     * Each Thread will have its own UnitOfWork
     */
    public static void newCurrent() {
        setCurrent(new UnitOfWork());
    }

    public static void setCurrent(UnitOfWork uow) {
        current.set(uow);
    }

    public static UnitOfWork getCurrent() {
        return current.get();
    }

    //TODO does it catch the concurrentyException?
    public void commit() {
        try {
            insertNew();
            updateDirty();
            deleteRemoved();
        } catch (ConcurrencyException e) {
            rollback();
            throw e;
        }
    }

    private void insertNew() {
        for (DomainObject obj : newObjects) {
            MapperRegistry.getMapper(obj.getClass()).insert(obj);
        }
    }

    private void updateDirty() {
        dirtyObjects
                .stream()
                .filter(domainObject -> !removedObjects.contains(domainObject))
                .forEach(domainObject -> MapperRegistry.getMapper(domainObject.getClass()).update(domainObject));
    }

    private void deleteRemoved() {
        for (DomainObject obj : removedObjects) {
            MapperRegistry.getMapper(obj.getClass()).delete(obj);
        }
    }

    /**
     * Removes the objects from the newObjects from the IdentityMap
     * Puts the objects in removedObjects into the IdentityMap
     * The objects in dirtyObjects need to go back as before
     */
    private void rollback(){
        /*for (DomainObject obj : newObjects)
            MapperRegistry.getMapper(obj.getClass()).getIdentityMap().remove(obj.getIdentityKey());*/

        newObjects
                .stream()
                .filter(domainObject -> MapperRegistry.getMapper(domainObject.getClass()).getIdentityMap().containsKey(domainObject.getIdentityKey()))
                .forEach(domainObject -> MapperRegistry.getMapper(domainObject.getClass()).getIdentityMap().remove(domainObject.getIdentityKey(), domainObject));

        for(DomainObject obj : dirtyObjects){
            clonedObjects
                    .stream()
                    .filter(domainObject -> domainObject.getIdentityKey().equals(obj.getIdentityKey()))
                    .findFirst()
                    .ifPresent(
                            clone -> MapperRegistry.getMapper(obj.getClass()).getIdentityMap().put(clone.getIdentityKey(), clone)
                    );
        }

        removedObjects
                .stream()
                .filter(obj -> !dirtyObjects.contains(obj))
                .forEach(obj -> MapperRegistry.getMapper(obj.getClass()).getIdentityMap().put(obj.getIdentityKey(), obj));
    }
}
