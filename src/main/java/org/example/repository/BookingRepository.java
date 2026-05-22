package org.example.repository;

import org.example.dto.Booking;

import java.util.List;

public interface BookingRepository {

    Booking create(Booking booking);
    Booking get(Long id);
    List<Booking> getAll();
    Booking update(Booking booking);
    void delete(Long id);
    List<Booking> getByWishId(Long wishId);
    // Получить все бронирования пользователя (что он дарит)
    List<Booking> getByUserId(Long userId);
    List<Booking> getByOwnerId(Long ownerId);
    // Удалить бронирование по wishId
    void deleteByWishId(Long wishId);


}
