package org.example.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/hello")
public class HelloController extends HttpServlet {
    @Override
    public void init() throws ServletException {
        // log.info("HelloServlet 初始化完成");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/plain;charset=UTF-8");
        try (PrintWriter out = resp.getWriter()) {
            out.println("hello");
        }
    }
}