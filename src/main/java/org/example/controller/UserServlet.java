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

import java.io.IOException;

@Log4j2
@WebServlet("/booking")
public class UserServlet extends HttpServlet {
    private UserService userService;
    public void init() throws ServletException {
        userService = ServiceConf.get(UserService.class);
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = Url.getId(req.getRequestURI());
        if (id != null) {
            resp.getWriter().print(Mapper.objectMapper.writeValueAsString(userService.get(id)));
            return;
        }
        resp.getWriter().print(Mapper.objectMapper.writeValueAsString(userService.getAll()));
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
        Long id = Url.getId(req.getRequestURI());
        if (id == null) {
            resp.setStatus(400);
            resp.getWriter().print("");
            return;
        }

        userService.delete(id);
        resp.setStatus(204);
        resp.getWriter().print("");
    }

}
