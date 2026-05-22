package org.example.service;

import org.example.dto.Person;
import org.example.repository.PersonRepository;

import java.util.List;

public class PersonServiceImpl implements PersonService {
    private final PersonRepository personRepository;
    public PersonServiceImpl(PersonRepository personRepository){
        this.personRepository = personRepository;
    }

    @Override
    public Person create(Person person) {
        return personRepository.create(person);
    }

    @Override
    public List<Person> getAll() {
        return personRepository.getAll();
    }

    @Override
    public Person get(Long id) {
        return personRepository.get(id);
    }

    @Override
    public Person update(Person person) {
        return personRepository.update(person);
    }

    @Override
    public void delete(Long id) {
        personRepository.delete(id);
    }
}
