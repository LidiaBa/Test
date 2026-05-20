package org.example.config;

import org.example.repository.PersonRepository;
import org.example.repository.PersonRepositoryJDBC;
import org.example.service.BookingService;
import org.example.service.BookingServiceImpl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceConf {
    private final static Map<Class<?>, Object> storage = new ConcurrentHashMap<>();
    static {
        PersonRepository st = new PersonRepositoryJDBC();
        storage.put(BookingService.class, new BookingServiceImpl(st));
    }

    public static <T> T get(String name, Class<T> tClass){
        if (!storage.containsKey(tClass)) return null;
        return (T)storage.get(tClass);
    }
}
