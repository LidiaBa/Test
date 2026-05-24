package org.example.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.example.config.ServiceConf;
import org.example.dto.User;
import org.example.dto.Wish;
import org.example.repository.AuthStorage;
import org.example.service.WishService;
import org.example.type.Mapper;
import org.example.type.Url;
import java.io.IOException;
import java.util.List;

@Log4j2
@WebServlet("/wishes/*")

public class WishServlet extends HttpServlet {
    private WishService wishService;
    public void init() throws ServletException {
        wishService = ServiceConf.get(WishService.class);
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        Long userId = (Long) req.getAttribute("userId");
        Long id = Url.getId(req.getRequestURI());

        if (id != null) {
            Wish wish = wishService.get(id);
            resp.getWriter().print(Mapper.objectMapper.writeValueAsString(wish));
        } else {
            // Только желания текущего пользователя
            List<Wish> wishes = wishService.getByUserId(userId);
            resp.getWriter().print(Mapper.objectMapper.writeValueAsString(wishes));
        }
    }


    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");

        Long userId = (Long) req.getAttribute("userId");
        System.out.println("UserId: " + userId);
        try {
            Wish wish = Mapper.objectMapper.readValue(req.getReader(), Wish.class);
            System.out.println("Parsed wish: " + wish);
            wish.setUserId(userId);
            wish = wishService.create(wish);
            resp.getWriter().print(Mapper.objectMapper.writeValueAsString(wish));
        } catch (Exception e){
            resp.setStatus(400);
            resp.getWriter().print("");
        }
    }

    @Override
    public void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        Long id = Url.getId(req.getRequestURI());
        Long userId = (Long) req.getAttribute("userId");
        if (id == null) {
            resp.setStatus(400);
            resp.getWriter().print("");
            return;
        }
        try {
            Wish wish = Mapper.objectMapper.readValue(req.getReader(), Wish.class);
            wish.setId(id);
            wish = wishService.update(wish, userId);
            resp.getWriter().print(Mapper.objectMapper.writeValueAsString(wish));
        } catch (Exception e) {
            resp.setStatus(400);
            resp.getWriter().print("");
        }
    }

    @Override
    public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        Long id = Url.getId(req.getRequestURI());
        Long userId = (Long) req.getAttribute("userId");
        System.out.println("Wish ID: " + id);
        System.out.println("User ID from attribute: " + userId);
        if (id == null) {
            resp.setStatus(400);
            resp.getWriter().print("");
            return;
        }
        if (userId == null) {
            User user = AuthStorage.currentUser.get();
            if (user != null) {
                userId = user.getId();
                System.out.println("UserId from ThreadLocal: " + userId);
            }
        }
        wishService.delete(id, userId);
        resp.setStatus(204);
        resp.getWriter().print("");
    }
 }

