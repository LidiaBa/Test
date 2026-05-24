package org.example.service;

import org.example.dto.Wish;

import java.util.List;

import lombok.extern.log4j.Log4j2;
import org.example.repository.WishRepository;

@Log4j2
public class WishServiceImpl implements WishService{

    private final WishRepository wishRepository;  // ← Добавьте это поле

    public WishServiceImpl(WishRepository wishRepository) {
        this.wishRepository = wishRepository;
    }

    @Override
    public Wish create(Wish wish) {
        if (wish.getTitle() == null || wish.getTitle().isBlank()) {
            throw new IllegalArgumentException("Wish title is required");
        }
        if (wish.getUserId() == null) {
            throw new IllegalArgumentException("User ID is required");
        }

        // По умолчанию статус FREE
        if (wish.getStatus() == null) {
            wish.setStatus("FREE");
        }

        log.debug("Creating wish for user: {}", wish.getUserId());
        return wishRepository.create(wish);
    }

    @Override
    public Wish get(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Wish ID is required");
        }

        Wish wish = wishRepository.get(id);
        if (wish == null) {
            throw new RuntimeException("Wish not found with id: " + id);
        }

        return wish;
    }

    @Override
    public List<Wish> getByUserId(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID is required");
        }

        return wishRepository.getByUserId(userId);
    }

    @Override
    public List<Wish> getAll() {
        return wishRepository.getAll();
    }

    @Override
    public Wish update(Wish wish, Long currentUserId) {
        if (wish.getId() == null) {
            throw new IllegalArgumentException("Wish ID is required for update");
        }

        // Проверяем, что пользователь — владелец желания
        Wish existing = get(wish.getId());
        if (!existing.getUserId().equals(currentUserId)) {
            throw new SecurityException("You can only edit your own wishes");
        }

        // Сохраняем оригинального владельца
        wish.setUserId(existing.getUserId());

        log.debug("Updating wish {} by user {}", wish.getId(), currentUserId);
        return wishRepository.update(wish);
    }

    @Override
    public void delete(Long id, Long currentUserId) {
        if (id == null) {
            throw new IllegalArgumentException("Wish ID is required");
        }
        if (currentUserId == null) {
            throw new IllegalArgumentException("User ID is required");
        }

        // Проверяем, что желание существует и пользователь — владелец
        Wish existing = get(id);
        if (existing == null) {
            throw new RuntimeException("Wish not found with id: " + id);
        }

        if (!existing.getUserId().equals(currentUserId)) {
            throw new SecurityException("You can only delete your own wishes");
        }

        log.debug("Deleting wish {} by user {}", id, currentUserId);
        wishRepository.delete(id);
    }

    @Override
    public void bookWish(Long wishId) {
        if (wishId == null) {
            throw new IllegalArgumentException("Wish ID is required");
        }

        Wish wish = get(wishId);

        if (!"FREE".equals(wish.getStatus())) {
            throw new IllegalStateException("Wish is already booked");
        }

        wishRepository.updateStatus(wishId, "BOOKED");
        log.debug("Wish {} status changed to BOOKED", wishId);
    }

    @Override
    public void unbookWish(Long wishId) {
        if (wishId == null) {
            throw new IllegalArgumentException("Wish ID is required");
        }

        Wish wish = get(wishId);

        if (!"BOOKED".equals(wish.getStatus())) {
            throw new IllegalStateException("Wish is not booked");
        }

        wishRepository.updateStatus(wishId, "FREE");
        log.debug("Wish {} status changed to FREE", wishId);
    }
}
