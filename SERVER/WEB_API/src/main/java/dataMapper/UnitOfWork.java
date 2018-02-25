package dataMapper;

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
        MapperRegistry.getMapper(obj).getIdentityMap().put(obj.getIdentityKey(), obj);
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
        MapperRegistry.getMapper(obj).getIdentityMap().remove(obj.getIdentityKey(), obj);
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
        MapperRegistry.getMapper(obj).getIdentityMap().put(obj.getIdentityKey(), obj);
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

    //TODO check versions
    public void commit() {
        insertNew();
        updateDirty();
        deleteRemoved();
    }

    private void insertNew() {
        for (DomainObject obj : newObjects) {
            MapperRegistry.getMapper(obj).insert(obj);
        }
    }

    private void updateDirty() {
        for (DomainObject obj : newObjects) {
            MapperRegistry.getMapper(obj).update(obj);
        }
    }

    private void deleteRemoved() {
        for (DomainObject obj : newObjects) {
            MapperRegistry.getMapper(obj).delete(obj);
        }
    }

    /**
     * Removes the objects from the newObjects from the IdentityMap
     * Puts the objects in removedObjects into the IdentityMap
     * The objects in dirtyObjects need to go back as before
     */
    public void rollback(){
        for (DomainObject obj : newObjects)
            MapperRegistry.getMapper(obj).getIdentityMap().remove(obj.getIdentityKey());

        for(DomainObject obj : dirtyObjects){
            clonedObjects
                    .stream()
                    .filter(domainObject -> domainObject.getIdentityKey().equals(obj.getIdentityKey()))
                    .findFirst()
                    .ifPresent(
                            (clone) -> MapperRegistry.getMapper(obj).getIdentityMap().put(clone.getIdentityKey(), clone)
                    );

        }

        removedObjects
                .stream()
                .filter(obj -> !dirtyObjects.contains(obj))
                .forEach(obj -> MapperRegistry.getMapper(obj).getIdentityMap().put(obj.getIdentityKey(), obj));
        /*for(DomainObject obj : removedObjects)
            if(!dirtyObjects.contains(obj))
                MapperRegistry.getMapper(obj).getIdentityMap().put(obj.getIdentityKey(), obj);
                */
    }
}
