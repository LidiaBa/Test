package org.example.repository;

import lombok.extern.log4j.Log4j2;
import org.example.dto.Person;
import org.example.dto.Wish;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@Log4j2
public class WishRepositoryJDBC implements WishRepository {
    private final DatabaseConnection connection = DatabaseConnection.getInstance();
    @Override
    public Wish create(Wish wish) {
        try(var st=connection.getStatement()) {
            String q =  String.format("insert into wishes (title, link, image_url, status, price,user_id) values (%s, %s, %s, %s,%d,%d)",
                    wish.getTitle(),
                    wish.getLink(),
                    wish.getImageUrl(),
                    wish.getStatus(),
                    wish.getPrice(),
                    wish.getUserId());
            log.debug(q);
            try (var rs = st.executeQuery(q)) {
                if (rs.rowInserted()) {
                    wish.setId(st.getGeneratedKeys().getLong(1));
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
            try (ResultSet rs = st.executeQuery(String.format("select * from wishes where id = %d", id))) {
                return Wish.builder().id(rs.getLong( "id"))
                        .title(rs.getString( "title"))
                        .link(rs.getString( "link"))
                        .imageUrl(rs.getString( "imageUrl"))
                        .status(rs.getString( "status"))
                        .price(rs.getInt( "price"))
                        .userId(rs.getLong( "userId")).build();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Wish> getAll() {
        try(var st = connection.getStatement()) {
            String sql;
            try(ResultSet rs = st.executeQuery( "select * from wishes")) {
                List<Wish> wishes = new ArrayList<>();
                while (rs.next()) {
                    wishes.add(Wish.builder().id(rs.getLong( "id")).
                            title(rs.getString( "title"))
                            .link(rs.getString( "link"))
                            .imageUrl(rs.getString( "imageUrl"))
                            .status(rs.getString( "status"))
                            .price(rs.getInt( "price"))
                            .userId(rs.getLong( "userid")).build());
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
            String q =  String.format("update wishes set title= %s, link= %s, imageUrl= %s, status= %s, price= %d, userid=%d where id=%d",
                    wish.getTitle(),
                    wish.getLink(),
                    wish.getImageUrl(),
                    wish.getStatus(),
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
            String q =  String.format("delete from wishes where id=%d", id);
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Wish> getByUserId(Long userId) {
        try (var st = connection.getStatement()) {
            String sql;
            try (ResultSet rs = st.executeQuery(String.format("select * from wishes where user_id =%d ", userId))) {
                List<Wish> wishes = new ArrayList<>();
                while (rs.next()) {
                    wishes.add(Wish.builder().id(rs.getLong("id"))
                            .title(rs.getString("title"))
                            .link(rs.getString("link"))
                            .imageUrl(rs.getString("imageUrl"))
                            .status(rs.getString( "status"))
                            .price(rs.getInt( "price"))
                            .userId(rs.getLong("userid")).build());
                }
                return wishes;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
