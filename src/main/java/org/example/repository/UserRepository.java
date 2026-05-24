package org.example.repository;

import org.example.dto.User;

import java.util.List;

public interface UserRepository {
    User create(User user);
    List<User> getAll();
    User get(Long id);
    User getByLogin(String login);
    User update(User user);
    void delete(Long id);

}
