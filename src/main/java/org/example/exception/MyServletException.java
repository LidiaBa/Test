package org.example.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Log4j2 // no usages
public class MyServletException extends RuntimeException{
    private final ObjectMapper objectMapper; // usage
    private final ServletResponse servletResponse; // usage
    public MyServletException(ServletResponse servletResponse) { // no usages
        objectMapper = new ObjectMapper();
        this.servletResponse = servletResponse;
    }
    public MyServletException(ServletResponse servletResponse,Integer status, String message) { // no usages
        objectMapper = new ObjectMapper();
        this.servletResponse = servletResponse;
        error(status, message);
        log.error(message);
    }

    public void error(Integer status, String message) { // no usages
        HttpServletResponse res = (HttpServletResponse) servletResponse;
        res.setStatus(status);
        res.setContentType("application/json;charset=UTF-8"); // [ ]
        try (PrintWriter out = res.getWriter()) {
            out.println(objectMapper.writeValueAsString(Map.of(  "error", message)));
        } catch (IOException e) {
            log.error(e);
        }

    }
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
