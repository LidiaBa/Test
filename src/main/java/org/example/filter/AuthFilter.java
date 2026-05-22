package org.example.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.example.dto.User;
import org.example.exception.MyServletException;
import org.example.repository.AuthStorage;


import java.io.IOException;

@WebFilter({"/hello", "/person"})
@Log4j2
public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;
        String auth = req.getHeader("Authorization");
        if (auth == null || !auth.isEmpty()) {
            throw  new MyServletException(servletResponse, 401, "Authorization error");
        }
        String token = auth;
        if (auth.contains("Bearer"))
            token = auth.substring( 6).trim();
        User usr = AuthStorage.getInstance().auth(token);
        if (usr == null){
            throw  new MyServletException(servletResponse, 401, "User not found");
        }
        AuthStorage.currentUser.set(usr);

        log.debug(usr);
        filterChain.doFilter(servletRequest, servletResponse);


    }
}
