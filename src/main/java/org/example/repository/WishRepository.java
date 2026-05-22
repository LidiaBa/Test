package org.example.repository;

import org.example.dto.Wish;
import java.util.List;

public interface WishRepository {

    Wish create(Wish wish);
    List<Wish> getAll();
    Wish get(Long id);
    Wish update(Wish wish);
    void updateStatus(Long id, String status);// Обновить статус желания (FREE/BOCKED)
    void delete(Long id);
    List<Wish> getByUserId(Long userId);// Получить все желания пользователя
}
