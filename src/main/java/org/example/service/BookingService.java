package org.example.service;

import org.example.dto.Booking;

import java.util.List;

public interface BookingService {

    Booking book(Long wishId, Long currentUserId);
    void cancel(Long wishId, Long currentUserId);
    List<Booking> getMyBookings(Long currentUserId);
    // Получить бронирования моих желаний (кто дарит мне)
    List<Booking> getBookingsForMe(Long currentUserId);
    // Получить бронирование по ID желания
    Booking getByWishId(Long wishId);
    // Проверить, забронировано ли желание
    boolean isBooked(Long wishId);
}
