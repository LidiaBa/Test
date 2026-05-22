package org.example.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.example.config.ServiceConf;
import org.example.dto.Person;
import org.example.service.PersonService;
import org.example.type.Mapper;
import org.example.type.Url;

import java.io.IOException;

@Log4j2
@WebServlet("/booking")
public class PersonServlet extends HttpServlet {
    private PersonService personService;
    public void init() throws ServletException {
        personService = ServiceConf.get(PersonService.class);
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = Url.getId(req.getRequestURI());
        if (id != null) {
            resp.getWriter().print(Mapper.objectMapper.writeValueAsString(personService.get(id)));
            return;
        }
        resp.getWriter().print(Mapper.objectMapper.writeValueAsString(personService.getAll()));
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Person person = Mapper.objectMapper.readValue(req.getReader(), Person.class);
            person = personService.create(person);
            resp.getWriter().print(Mapper.objectMapper.writeValueAsString(person));
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
            Person person = Mapper.objectMapper.readValue(req.getReader(), Person.class);
            person.setId(id);
            person = personService.update(person);
            resp.getWriter().print(Mapper.objectMapper.writeValueAsString(person));
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

        personService.delete(id);
        resp.setStatus(204);
        resp.getWriter().print("");
    }

}
