package org.example.config;

import org.example.repository.PersonRepository;
import org.example.repository.PersonRepositoryJDBC;
import org.example.service.BookingService;
import org.example.service.BookingServiceImpl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class ServiceConf {  // 2 usages
    private final static Map<Class<?>, Object> storage = new ConcurrentHashMap<>();

    static {
        storage.put(PersonRepository.class, new PersonRepositoryJDBC());
        storage.put(BookingService.class, new BookingServiceImpl(get(PersonRepository.class)));
    }

    public static <T> T get(Class<T> tClass) {  // 1 usage
        if (!storage.containsKey(tClass)) return null;
        return (T)storage.get(tClass);
    }
}