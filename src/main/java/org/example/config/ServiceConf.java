package org.example.config;

import org.example.repository.*;
import org.example.service.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceConf {
    private static final class StorageHolder {  // usage
        private static final Map<Class<?>, Object> STORAGE = init();  // usage

        private static Map<Class<?>, Object> init() {  // usage
            Map<Class<?>, Object> storage = new ConcurrentHashMap<>();

            UserRepository userRepository = new UserRepositoryJDBC();
            WishRepository wishRepository = new WishRepositoryJDBC();
            BookingRepository bookingRepository = new BookingRepositoryJDBC();

            UserService userService = new UserServiceImpl(userRepository);
            WishService wishService = new WishServiceImpl(wishRepository);
            BookingService bookingService = new BookingServiceImpl(bookingRepository, wishRepository);

            storage.put(UserRepository.class, userRepository);
            storage.put(WishRepository.class, wishRepository);
            storage.put(BookingRepository.class, bookingRepository);

            storage.put(UserService.class, userService);
            storage.put(WishService.class, wishService);
            storage.put(BookingService.class, bookingService);
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

