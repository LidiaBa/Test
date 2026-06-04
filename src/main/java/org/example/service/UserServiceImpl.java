package org.example.service;

import org.example.dto.User;
import org.example.repository.UserRepository;

import java.util.List;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public User create(User user) {

        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        if (user.getLogin() == null || user.getLogin().isBlank()) {
            throw new IllegalArgumentException("Login is required");
        }

        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }

        User existing = userRepository.getByLogin(user.getLogin());
        if (existing != null) {
            throw new IllegalStateException("User with login '" + user.getLogin() + "' already exists");
        }

        return userRepository.create(user);
    }

    @Override
    public List<User> getAll() {
        List<User> users = userRepository.getAll();
        users.forEach(u -> u.setPassword(null));
        return users;
    }

    @Override
    public User get(Long id) {

        if (id == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }


        User user = userRepository.get(id);
        if (user == null) {
            throw new RuntimeException("User not found with id: " + id);
        }

        user.setPassword(null);
        return user;
    }

    @Override
    public User update(User user) {

        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        if (user.getId() == null) {
            throw new IllegalArgumentException("User ID is required for update");
        }

        User existing = userRepository.get(user.getId());
        if (existing == null) {
            throw new RuntimeException("User not found with id: " + user.getId());
        }

        if (user.getName() != null && user.getName().isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }

        user.setLogin(existing.getLogin());

        User updated = userRepository.update(user);
        updated.setPassword(null);  // скрываем пароль
        return updated;
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        User existing = userRepository.get(id);
        if (existing == null) {
            throw new RuntimeException("User not found with id: " + id);
        }

        userRepository.delete(id);
    }

}