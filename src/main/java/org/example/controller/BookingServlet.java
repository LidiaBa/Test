package org.example.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.example.config.ServiceConf;
import org.example.dto.Booking;

import org.example.service.BookingService;

import org.example.type.Mapper;
import org.example.type.Url;
import java.io.IOException;
import java.util.List;
@Log4j2
@WebServlet("/wishes")

public class BookingServlet extends HttpServlet {
    private BookingService bookingService;
    public void init() throws ServletException {
        bookingService = ServiceConf.get(BookingService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json;charset=UTF-8");
        Long userId = (Long) req.getAttribute("userId");
        String path = req.getPathInfo();

        try {
            if ("/for-me".equals(path)) {
                List<Booking> bookings = bookingService.getBookingsForMe(userId);
                resp.getWriter().print(Mapper.objectMapper.writeValueAsString(bookings));
            } else {
                List<Booking> bookings = bookingService.getMyBookings(userId);
                resp.getWriter().print(Mapper.objectMapper.writeValueAsString(bookings));
            }
        } catch (Exception e) {
            resp.setStatus(500);
            resp.getWriter().print("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    // POST /bookings?wishId={id} - забронировать желание
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json;charset=UTF-8");
        Long userId = (Long) req.getAttribute("userId");
        String wishIdParam = req.getParameter("wishId");

        if (wishIdParam == null) {
            resp.setStatus(400);
            resp.getWriter().print("{\"error\":\"wishId parameter is required\"}");
            return;
        }

        try {
            Long wishId = Long.parseLong(wishIdParam);
            Booking booking = bookingService.book(wishId, userId);
            resp.setStatus(201);
            resp.getWriter().print(Mapper.objectMapper.writeValueAsString(booking));
        } catch (NumberFormatException e) {
            resp.setStatus(400);
            resp.getWriter().print("{\"error\":\"Invalid wishId format\"}");
        } catch (IllegalAccessError e) {
            resp.setStatus(403);
            resp.getWriter().print("{\"error\":\"" + e.getMessage() + "\"}");
        } catch (IllegalStateException e) {
            resp.setStatus(409);
            resp.getWriter().print("{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            resp.setStatus(400);
            resp.getWriter().print("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    // DELETE /bookings?wishId={id} - отменить бронирование
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Long userId = (Long) req.getAttribute("userId");
        String wishIdParam = req.getParameter("wishId");

        if (wishIdParam == null) {
            resp.setStatus(400);
            resp.getWriter().print("{\"error\":\"wishId parameter is required\"}");
            return;
        }

        try {
            Long wishId = Long.parseLong(wishIdParam);
            bookingService.cancel(wishId, userId);
            resp.setStatus(204);
        } catch (NumberFormatException e) {
            resp.setStatus(400);
            resp.getWriter().print("{\"error\":\"Invalid wishId format\"}");
        } catch (SecurityException e) {
            resp.setStatus(403);
            resp.getWriter().print("{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            resp.setStatus(400);
            resp.getWriter().print("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}