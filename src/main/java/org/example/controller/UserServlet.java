package org.example.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.example.config.ServiceConf;
import org.example.dto.User;
import org.example.service.UserService;
import org.example.type.Mapper;
import org.example.type.Url;

import java.io.BufferedReader;
import java.io.IOException;

@Log4j2
@WebServlet("/user/*")
public class UserServlet extends HttpServlet {
    private UserService userService;
    public void init() throws ServletException {
        userService = ServiceConf.get(UserService.class);
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        Long id = Url.getId(req.getRequestURI());
        if (id != null) {
            resp.getWriter().print(Mapper.objectMapper.writeValueAsString(userService.get(id)));
            return;
        }
        resp.getWriter().print(Mapper.objectMapper.writeValueAsString(userService.getAll()));
    }

   @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        try {
            User user = Mapper.objectMapper.readValue(req.getReader(), User.class);
            user = userService.create(user);
            resp.getWriter().print(Mapper.objectMapper.writeValueAsString(user));
        } catch (Exception e){
            resp.setStatus(400);
            resp.getWriter().print("");
        }
    }


    @Override
    public void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        Long id = Url.getId(req.getRequestURI());
        if (id == null) {
            resp.setStatus(400);
            resp.getWriter().print("");
        }
        try {
            User user = Mapper.objectMapper.readValue(req.getReader(), User.class);
            user.setId(id);
            user = userService.update(user);
            resp.getWriter().print(Mapper.objectMapper.writeValueAsString(user));
        } catch (Exception e) {
            resp.setStatus(400);
            resp.getWriter().print("");
        }
    }

    @Override
    public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");

        Long id = Url.getId(req.getRequestURI());
        Long userId = (Long) req.getAttribute("userId");
        String userRole = (String) req.getAttribute("userRole");  // ← добавить

        if (id == null) {
            resp.setStatus(400);
            resp.getWriter().print("{\"error\":\"Invalid user ID\"}");
            return;
        }

        if (!"ADMIN".equals(userRole) && !id.equals(userId)) {
            resp.setStatus(403);
            resp.getWriter().print("{\"error\":\"You can only delete your own account\"}");
            return;
        }

        if ("ADMIN".equals(userRole) && id.equals(userId)) {
            // Админ удаляет сам себя — можно разрешить, но с осторожностью
            log.warn("Admin {} is deleting their own account", userId);
        }

        userService.delete(id);
        resp.setStatus(204);
    }

}
