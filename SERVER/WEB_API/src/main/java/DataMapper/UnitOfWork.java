package DataMapper;

import io.jsonwebtoken.lang.Assert;

import java.util.ArrayList;
import java.util.List;

public class UnitOfWork {
    private List<DomainObject> newObjects = new ArrayList();
    private List<DomainObject> dirtyObjects = new ArrayList();
    private List<DomainObject> removedObjects = new ArrayList();

    public void registerNew(DomainObject obj) {
        Assert.notNull(obj.getId(), "id not null");
        Assert.isTrue(!dirtyObjects.contains((obj)), "object not dirty");
        Assert.isTrue(!removedObjects.contains(obj), "object not removed");
        Assert.isTrue(!newObjects.contains(obj), "object not already registered new");
        newObjects.add(obj);
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
