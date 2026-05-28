package org.example.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.config.ServiceConf;
import org.example.dto.User;
import org.example.dto.Wish;
import org.example.service.UserService;
import org.example.service.WishService;
import org.example.type.Mapper;

import java.io.IOException;
import java.util.List;

@WebServlet("/users/*")
public class UserWishesServlet extends HttpServlet {
    private UserService userService;
    private WishService wishService;

    @Override
    public void init() throws ServletException {
        userService = ServiceConf.get(UserService.class);
        wishService = ServiceConf.get(WishService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String pathInfo = req.getPathInfo();

        // GET /users - список всех пользователей
        if (pathInfo == null || pathInfo.equals("/")) {
            List<User> users = userService.getAll();
            // Скрываем пароли
            users.forEach(user -> user.setPassword(null));
            resp.getWriter().print(Mapper.objectMapper.writeValueAsString(users));
            return;
        }

        String[] parts = pathInfo.split("/");

        // GET /users/{id}/wishes - желания пользователя
        if (parts.length >= 3 && "wishes".equals(parts[2])) {
            try {
                Long userId = Long.parseLong(parts[1]);
                List<Wish> wishes = wishService.getByUserId(userId);
                resp.getWriter().print(Mapper.objectMapper.writeValueAsString(wishes));
            } catch (NumberFormatException e) {
                resp.setStatus(400);
                resp.getWriter().print("{\"error\":\"Invalid user ID format\"}");
            }
            return;
        }

        // GET /users/{id} - конкретный пользователь
        if (parts.length >= 2) {
            try {
                Long userId = Long.parseLong(parts[1]);
                User user = userService.get(userId);
                if (user == null) {
                    resp.setStatus(404);
                    resp.getWriter().print("{\"error\":\"User not found\"}");
                    return;
                }
                user.setPassword(null);
                resp.getWriter().print(Mapper.objectMapper.writeValueAsString(user));
            } catch (NumberFormatException e) {
                resp.setStatus(400);
                resp.getWriter().print("{\"error\":\"Invalid user ID format\"}");
            }
            return;
        }

        // Если ничего не подошло
        resp.setStatus(404);
        resp.getWriter().print("{\"error\":\"Not found\"}");
    }
}