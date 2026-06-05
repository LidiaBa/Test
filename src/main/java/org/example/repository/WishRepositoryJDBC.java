package org.example.repository;

import lombok.extern.log4j.Log4j2;
import org.example.dto.Wish;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


@Log4j2
public class WishRepositoryJDBC implements WishRepository {
    private final DatabaseConnection connection = DatabaseConnection.getInstance();
    @Override
    public Wish create(Wish wish) {
        try (var st = connection.getStatement()) {
            String q = String.format("INSERT INTO wishes (title, link, status, price, user_id) VALUES ('%s', '%s', '%s', %d, %d)",
                    wish.getTitle(),
                    wish.getLink() != null ? wish.getLink() : "",
                    wish.getStatus() != null ? wish.getStatus() : "FREE",
                    wish.getPrice() != null ? wish.getPrice() : 0,
                    wish.getUserId());
            log.debug(q);

            int affected = st.executeUpdate(q, Statement.RETURN_GENERATED_KEYS);
            System.out.println("Affected rows: " + affected);

            if (affected > 0) {
                try (var rs = st.getGeneratedKeys()) {
                    if (rs.next()) {
                        wish.setId(rs.getLong(1));
                        System.out.println("Generated ID: " + wish.getId());
                    }
                }
            }
            return wish;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Wish get(Long id) {
        try (var st = connection.getStatement()) {
            try (ResultSet rs = st.executeQuery(String.format("select * from wishes where id = '%d'", id))) {
                if (rs.next()) {
                    return Wish.builder().id(rs.getLong("id"))
                            .title(rs.getString("title"))
                            .link(rs.getString("link"))
                            .status(rs.getString("status"))
                            .price(rs.getInt("price"))
                            .userId(rs.getLong("user_Id")).build();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public List<Wish> getAll() {
        try(var st = connection.getStatement()) {
            //String sql;
            try(ResultSet rs = st.executeQuery( "SELECT * FROM wishes")) {
                List<Wish> wishes = new ArrayList<>();
                while (rs.next()) {
                    wishes.add(Wish.builder().id(rs.getLong( "id")).
                            title(rs.getString( "title"))
                            .link(rs.getString( "link"))
                            .status(rs.getString( "status"))
                            .price(rs.getInt( "price"))
                            .userId(rs.getLong( "user_id")).build());
                }
                return wishes;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Wish update(Wish wish) {
        try(var st=connection.getStatement()) {

            String q = String.format("update wishes set title = '%s', link = '%s', price = %d, user_id = %d where id = %d",
                    wish.getTitle(),
                    wish.getLink() != null ? wish.getLink() : "null",
                    wish.getPrice(),
                    wish.getUserId(),
                    wish.getId());
            log.debug(q);
            st.execute(q);
            return wish;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Long id) {
        try(var st=connection.getStatement()) {
            String q =  String.format("delete from wishes where id='%d'", id);
            log.debug(q);
            st.execute(q);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateStatus(Long id, String status) {
        try (var st = connection.getStatement()) {
            String q = String.format("UPDATE wishes SET status = '%s' WHERE id = %d", status, id);
            log.debug(q);
            st.execute(q);

            System.out.println("=== updateStatus ===");
            System.out.println("Wish ID: " + id);
            System.out.println("New status: " + status);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Wish> getByUserId(Long userId) {
        try (var st = connection.getStatement()) {
            //String sql;
            try (ResultSet rs = st.executeQuery(String.format("select * from wishes where user_id ='%d' ", userId))) {
                List<Wish> wishes = new ArrayList<>();
                while (rs.next()) {
                    wishes.add(Wish.builder().id(rs.getLong("id"))
                            .title(rs.getString("title"))
                            .link(rs.getString("link"))
                            .status(rs.getString( "status"))
                            .price(rs.getInt( "price"))
                            .userId(rs.getLong("user_id")).build());
                }
                return wishes;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
