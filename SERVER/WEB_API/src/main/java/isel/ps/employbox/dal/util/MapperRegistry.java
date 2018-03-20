package isel.ps.employbox.dal.util;

import isel.ps.employbox.dal.Mapper;
import isel.ps.employbox.dal.model.DomainObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MapperRegistry {
    private static Map<Class, Mapper> map = new HashMap<>();

    public static Optional<Mapper> getMapper(Class domainObject) {
        return Optional.ofNullable(map.get(domainObject));
    }

    public static <T extends DomainObject<K>, K> void addEntry(Class<T> domainObjectClass, Mapper<T, K> dataMapper) {
        map.put(domainObjectClass, dataMapper);
    }
}
