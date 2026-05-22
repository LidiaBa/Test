package org.example.service;

import org.example.dto.User;

import java.util.List;

public interface UserService {
    User create(User user);
    List<User> getAll();
    User get(Long id);
    User update(User user);
    void delete(Long id);
}
