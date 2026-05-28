package org.example.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dto.Token;
import org.example.exception.MyServletException;
import org.example.repository.AuthStorage;
import org.example.type.Mapper;

import java.io.IOException;
import java.util.Map;

@WebServlet("/auth")
public class AuthServlet extends HelloServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        if (login == null || login.isEmpty()) {
            throw new MyServletException(resp, 401, "Не передана login");
        }

        if (password == null || password.isEmpty()) {
            throw new MyServletException(resp, 401, "Не передана password");
        }

        Token token = AuthStorage.getInstance().auth(login, password);
        if (token == null) {
            throw new MyServletException(resp, 401, "Не найдено пользователя");
        }
        resp.setStatus(200);
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().print(Mapper.objectMapper.writeValueAsString(Map.of( "token", token)));
    }
}
