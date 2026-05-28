package org.example.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.config.ServiceConf;
import org.example.dto.User;
import org.example.service.UserService;
import org.example.type.Mapper;

import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = ServiceConf.get(UserService.class);
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

}
