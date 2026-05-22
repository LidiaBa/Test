package org.example.service;

import org.example.dto.Person;

import java.util.List;

public interface PersonService {
    Person create(Person person);
    List<Person> getAll();
    Person get(Long id);
    Person update(Person person);
    void delete(Long id);
}
