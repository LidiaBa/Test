package org.example.repository;

import lombok.extern.log4j.Log4j2;
import org.example.dto.Person;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@Log4j2
public class PersonRepositoryJDBC implements PersonRepository {
    private final DatabaseConnection connection = DatabaseConnection.getInstance();

    @Override
    public Person create(Person person) {
       try(var st=connection.getStatement()) {
        String q =  String.format("insert into person (name, email) values (%s, %s)", person.getName(),person.getEmail());
        log.debug(q);
        try (var rs = st.executeQuery(q)) {
            if (rs.rowInserted()) {
                person.setId(st.getGeneratedKeys().getLong(1));
            }
        }
        return person;
       } catch (SQLException e) {
           throw new RuntimeException(e);
       }
    }

    @Override
    public List<Person> getAll() {
        try(var st = connection.getStatement()) {
            String sql;
            try(ResultSet rs = st.executeQuery( "select * from person")) {
                List<Person> persons = new ArrayList<>();
                while (rs.next()) {
                    persons.add(Person.builder().id(rs.getLong( "id")).name(rs.getString( "name"))
    .email(rs.getString( "email")).build());
                }
                return persons;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Person get(Long id) {
        try (var st = connection.getStatement()) {
            try (ResultSet rs = st.executeQuery(String.format("select * from student where id = %d", id))) {
                return Person.builder().id(rs.getLong( "id")).name(rs.getString( "name"))
    .email(rs.getString( "email")).build();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Person update(Person person) {
        try(var st=connection.getStatement()) {
            String q =  String.format("update person %s name= %s, email= %s, phone= %s where id=%d",
                    person.getName(),person.getEmail());
            log.debug(q);
            st.execute(q);
            return person;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Long id) {
        try(var st=connection.getStatement()) {
            String q =  String.format("delete from person where id=%d", id);
            log.debug(q);
            st.execute(q);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
