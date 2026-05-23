package org.example.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.example.config.ServiceConf;
import org.example.dto.Wish;
import org.example.service.WishService;
import org.example.type.Mapper;
import org.example.type.Url;
import java.io.IOException;

@Log4j2
@WebServlet("/wishes")

public class WishServlet extends HttpServlet {
    private WishService wishService;
    public void init() throws ServletException {
        wishService = ServiceConf.get(WishService.class);
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        Long id = Url.getId(req.getRequestURI());
        if (id != null) {
            resp.getWriter().print(Mapper.objectMapper.writeValueAsString(wishService.get(id)));
            return;
        }
        resp.getWriter().print(Mapper.objectMapper.writeValueAsString(wishService.getAll()));
    }


    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        try {
            Wish wish = Mapper.objectMapper.readValue(req.getReader(), Wish.class);
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
        if (id == null) {
            resp.setStatus(400);
            resp.getWriter().print("");
            return;
        }

        wishService.delete(id, userId);
        resp.setStatus(204);
        resp.getWriter().print("");
    }
 }

