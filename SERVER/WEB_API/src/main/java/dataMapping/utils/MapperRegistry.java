package dataMapping.utils;

import dataMapping.mappers.AbstractMapper;
import model.DomainObject;

import java.util.HashMap;
import java.util.Map;

public class MapperRegistry {
    private static Map<Class, AbstractMapper> map = new HashMap<>();

    public static AbstractMapper getMapper(Class domainObject) {
        return map.get(domainObject);
    }

    public static <T extends DomainObject> void addEntry(Class<T> domainObjectClass, AbstractMapper<T> abstractMapper) {
        map.put(domainObjectClass, abstractMapper);
    }
}
