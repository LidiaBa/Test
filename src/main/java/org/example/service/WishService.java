package org.example.service;

import org.example.dto.Wish;

import java.util.List;

public interface WishService {

    Wish create(Wish wish);
    Wish get(Long id);
    List<Wish> getByUserId(Long userId);
    List<Wish> getAll();
    Wish update(Wish wish, Long currentUserId);
    void delete(Long id, Long currentUserId);
    void bookWish(Long wishId);
    void unbookWish(Long wishId);
}