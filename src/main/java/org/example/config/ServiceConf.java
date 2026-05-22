package org.example.config;

import org.example.repository.PersonRepository;
import org.example.repository.PersonRepositoryJDBC;
import org.example.service.PersonService;
import org.example.service.PersonServiceImpl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceConf {
    private static final class StorageHolder {  // usage
        private static final Map<Class<?>, Object> STORAGE = init();  // usage

        private static Map<Class<?>, Object> init() {  // usage
            Map<Class<?>, Object> storage = new ConcurrentHashMap<>();

            PersonRepository studentRepository = new PersonRepositoryJDBC();
            PersonService studentService = new PersonServiceImpl(studentRepository);
            storage.put(PersonRepository.class, studentRepository);
            storage.put(PersonService.class, studentService);
            return storage;
        }
    }

    public static <T> T get(Class<T> tClass) {
        Object instance = StorageHolder.STORAGE.get(tClass);
        if (instance == null) return null;
        if (!tClass.isInstance(instance)) return null;
        return tClass.cast(instance);
    }
}

/*public class ServiceConf {  // 2 usages
    private final static Map<Class<?>, Object> storage = new ConcurrentHashMap<>();

    static {
        storage.put(PersonRepository.class, new PersonRepositoryJDBC());
        storage.put(PersonService.class, new BookingServiceImpl(get(PersonRepository.class)));
    }

    public static <T> T get(Class<T> tClass) {  // 1 usage
        if (!storage.containsKey(tClass)) return null;
        return (T)storage.get(tClass);
    }
} */