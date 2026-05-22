package org.example.service;

import lombok.extern.log4j.Log4j2;
import org.example.dto.Booking;
import org.example.dto.Wish;
import org.example.repository.BookingRepository;
import org.example.repository.WishRepository;

import java.util.List;

@Log4j2

public class BookingServiceImpl implements BookingService{
    private final BookingRepository bookingRepository;
    private final WishRepository wishRepository;

    // Конструктор
    public BookingServiceImpl(BookingRepository bookingRepository,
                              WishRepository wishRepository) {
        this.bookingRepository = bookingRepository;
        this.wishRepository = wishRepository;
    }

    @Override
    public Booking book(Long wishId, Long currentUserId) {
        // Проверка параметров
        if (wishId == null) {
            throw new IllegalArgumentException("Wish ID is required");
        }
        if (currentUserId == null) {
            throw new IllegalArgumentException("User ID is required");
        }

        // Получаем желание
        Wish wish = wishRepository.get(wishId);
        if (wish == null) {
            throw new RuntimeException("Wish not found with id: " + wishId);
        }

        // Нельзя забронировать своё желание
        if (wish.getUserId().equals(currentUserId)) {
            throw new IllegalAccessError("You cannot book your own wish");
        }

        // Нельзя забронировать уже забронированное
        if (!"FREE".equals(wish.getStatus())) {
            throw new IllegalStateException("This wish is already booked");
        }

        // Создаём бронирование
        Booking booking = Booking.builder()
                .wishId(wishId)
                .userId(currentUserId)
                .ownerId(wish.getUserId())
                .build();

        booking = bookingRepository.create(booking);

        // Меняем статус желания на BOOKED
        wishRepository.updateStatus(wishId, "BOOKED");

        log.info("User {} booked wish {} from user {}", currentUserId, wishId, wish.getUserId());
        return booking;
    }

    @Override
    public void cancel(Long wishId, Long currentUserId) {
        // Проверка параметров
        if (wishId == null) {
            throw new IllegalArgumentException("Wish ID is required");
        }
        if (currentUserId == null) {
            throw new IllegalArgumentException("User ID is required");
        }

        // Проверяем, существует ли бронирование
        List<Booking> bookings = bookingRepository.getByWishId(wishId);
        if (bookings == null || bookings.isEmpty()) {
            throw new RuntimeException("No booking found for wish: " + wishId);
        }

        Booking booking = bookings.get(0);

        // Только тот, кто забронировал, может отменить
        if (!booking.getUserId().equals(currentUserId)) {
            throw new SecurityException("You can only cancel your own bookings");
        }

        // Удаляем бронирование
        bookingRepository.deleteByWishId(wishId);

        // Меняем статус желания обратно на FREE
        wishRepository.updateStatus(wishId, "FREE");

        log.info("User {} cancelled booking for wish {}", currentUserId, wishId);
    }

    @Override
    public List<Booking> getMyBookings(Long currentUserId) {
        if (currentUserId == null) {
            throw new IllegalArgumentException("User ID is required");
        }

        return bookingRepository.getByUserId(currentUserId);
    }

    @Override
    public List<Booking> getBookingsForMe(Long currentUserId) {
        if (currentUserId == null) {
            throw new IllegalArgumentException("User ID is required");
        }

        return bookingRepository.getByOwnerId(currentUserId);
    }

    @Override
    public Booking getByWishId(Long wishId) {
        if (wishId == null) {
            throw new IllegalArgumentException("Wish ID is required");
        }

        List<Booking> bookings = bookingRepository.getByWishId(wishId);
        if (bookings == null || bookings.isEmpty()) {
            return null;
        }

        return bookings.get(0);
    }

    @Override
    public boolean isBooked(Long wishId) {
        return getByWishId(wishId) != null;
    }
}
