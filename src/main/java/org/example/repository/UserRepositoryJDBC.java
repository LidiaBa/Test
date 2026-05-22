package org.example.repository;

import lombok.extern.log4j.Log4j2;
import org.example.dto.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@Log4j2
public class UserRepositoryJDBC implements UserRepository {
    private final DatabaseConnection connection = DatabaseConnection.getInstance();

    @Override
    public User create(User user) {
       try(var st=connection.getStatement()) {
        String q =  String.format("insert into users (name) values (%s)", user.getName());
        log.debug(q);
        try (var rs = st.executeQuery(q)) {
            if (rs.rowInserted()) {
                user.setId(st.getGeneratedKeys().getLong(1));
            }
        }
        return user;
       } catch (SQLException e) {
           throw new RuntimeException(e);
       }
    }

    @Override
    public List<User> getAll() {
        try(var st = connection.getStatement()) {
            try(ResultSet rs = st.executeQuery( "select * from users")) {
                List<User> users = new ArrayList<>();
                while (rs.next()) {
                    users.add(User.builder().id(rs.getLong( "id")).name(rs.getString( "name")).build());
                }
                return users;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User get(Long id) {
        try (var st = connection.getStatement()) {
            try (ResultSet rs = st.executeQuery(String.format("select * from users where id = %d", id))) {
                return User.builder().id(rs.getLong( "id")).name(rs.getString( "name")).build();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public User update(User user) {
        try(var st=connection.getStatement()) {
            String q =  String.format("update users set name= %s where id=%d",
                    user.getName(),
                    user.getId());
            log.debug(q);
            st.execute(q);
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Long id) {
        try(var st=connection.getStatement()) {
            String q =  String.format("delete from users where id=%d", id);
            log.debug(q);
            st.execute(q);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
