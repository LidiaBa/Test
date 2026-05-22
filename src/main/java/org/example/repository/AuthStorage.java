package org.example.repository;

import org.example.dto.Token;
import org.example.dto.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AuthStorage { // usages
    private static final AuthStorage instance = new AuthStorage(); // usage
    private final Map<String, Token> tokens = new ConcurrentHashMap<>(0); // no usages
    private static final List<User> users = List.of( // no usages
            User.builder().id(1L).login("nx").name("Baca").password("12345678").build()
    );
    public static ThreadLocal<User> currentUser = new ThreadLocal<>();
    private AuthStorage() {}

    public static AuthStorage getInstance() {
        return instance;
    }
    public Token auth(String login, String password) { // no usages
        for(User user: users){
            if (user.getLogin().equals(login) && user.getPassword().equals(password)){
                return generate(user);
            }
        }
        return null;
    }

    public User auth(String token){
        if (tokens.containsKey(token) && tokens.get(token).getExp().isAfter(LocalDateTime.now())) {
            return tokens.get(token).getUser();
        }
        return null;
    }

    private Token generate(User user) {
        String token = UUID.randomUUID().toString();
        Token t = Token.builder()
                .user(user)
                .exp(LocalDateTime.now().plusHours(4))
                .token(token)
                .build();
        tokens.put(token, t);
        return t;
    }
}