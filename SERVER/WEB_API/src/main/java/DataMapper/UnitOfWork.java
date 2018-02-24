package DataMapper;

import java.util.ArrayList;
import java.util.List;

public class UnitOfWork {
    private List<DomainObject> newObjects = new ArrayList();
    private List<DomainObject> dirtyObjects = new ArrayList();
    private List<DomainObject> removedObjects = new ArrayList();

    //TODO register with Identity Map
    public void registerNew(DomainObject obj) {
        assert obj.getId() != null;
        assert !dirtyObjects.contains(obj);
        assert !removedObjects.contains(obj);
        assert !newObjects.contains(obj);
        newObjects.add(obj);
    }

    public void registerDirty(DomainObject obj){
        assert obj.getId()!= null;
        assert !removedObjects.contains(obj);
        if(!dirtyObjects.contains(obj) && !newObjects.contains(obj))
            dirtyObjects.add(obj);
    }

    public void registerRemoved(DomainObject obj){
        assert obj.getId()!= null;
        if(newObjects.remove(obj)) return;
        dirtyObjects.remove(obj);
        if(!removedObjects.contains(obj))
            removedObjects.add(obj);
    }

    public void registerClean(DomainObject obj){
        assert obj.getId()!= null;
    }

    private static ThreadLocal<UnitOfWork> current = new ThreadLocal<>();

    public static void newCurrent() {
        setCurrent(new UnitOfWork());
    }

    public static void setCurrent(UnitOfWork uow) {
        current.set(uow);
    }

    public static UnitOfWork getCurrent() {
        return current.get();
    }

    public void commit() {
        insertNew();
        updateDirty();
        deleteRemoved();
    }

    private void insertNew() {

    }

    private void updateDirty() {

    }

    private void deleteRemoved() {

    }
}
