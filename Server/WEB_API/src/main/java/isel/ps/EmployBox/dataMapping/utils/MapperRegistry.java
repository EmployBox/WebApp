package isel.ps.EmployBox.dataMapping.utils;

import isel.ps.EmployBox.dataMapping.mappers.AbstractMapper;
import isel.ps.EmployBox.model.DomainObject;

import java.util.HashMap;
import java.util.Map;

public class MapperRegistry {
    private static Map<Class, AbstractMapper> map = new HashMap<>();

    public static AbstractMapper getMapper(Class domainObject) {
        return map.get(domainObject);
    }

    public static <T extends DomainObject<K>, K> void addEntry(Class<T> domainObjectClass, AbstractMapper<T, K> abstractMapper) {
        map.put(domainObjectClass, abstractMapper);
    }
}
