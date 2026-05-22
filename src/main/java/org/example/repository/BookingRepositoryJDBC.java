package org.example.repository;
import lombok.extern.log4j.Log4j2;
import lombok.extern.log4j.Log4j2;
import org.example.dto.Booking;
import org.example.dto.Wish;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
public class BookingRepositoryJDBC implements BookingRepository{
    private final DatabaseConnection connection = DatabaseConnection.getInstance();

    @Override
    public Booking create(Booking booking) {
        try(var st=connection.getStatement()) {
            String q =  String.format("insert into bookings (wish_id, user_id, owner_id) values (%d, %d,%d)",
                    booking.getWishId(),
                    booking.getUserId(),
                    booking.getOwnerId());
           // log.debug(q);
            try (var rs = st.executeQuery(q)) {
                if (rs.rowInserted()) {
                    booking.setId(st.getGeneratedKeys().getLong(1));
                }
            }
            return booking;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Booking get(Long id) {
        try (var st = connection.getStatement()) {
            try (ResultSet rs = st.executeQuery(String.format("select * from bookings where id = %d", id))) {
                return Booking.builder().id(rs.getLong( "id"))
                        .userId(rs.getLong( "wishId"))
                        .userId(rs.getLong( "userId"))
                        .userId(rs.getLong( "ownerId")).build();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Booking> getAll() {
        try(var st = connection.getStatement()) {
            String sql;
            try(ResultSet rs = st.executeQuery( "select * from bookings")) {
                List<Booking> bookings = new ArrayList<>();
                while (rs.next()) {
                    bookings.add(Booking.builder().id(rs.getLong( "id"))
                            .userId(rs.getLong( "wishId"))
                            .userId(rs.getLong( "userId"))
                            .userId(rs.getLong( "ownerId")).build());
                }
                return bookings;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Booking update(Booking booking) {
        try(var st=connection.getStatement()) {
            String q =  String.format("update bookings set wish_id= %d, user_id= %d, owner_id=%d where id=%d",
                    booking.getWishId(),
                    booking.getUserId(),
                    booking.getOwnerId(),
                    booking.getId());
          //  log.debug(q);
            st.execute(q);
            return booking;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Long id) {
        try(var st=connection.getStatement()) {
            String q =  String.format("delete from bookings where id=%d", id);
            //log.debug(q);
            st.execute(q);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Booking> getByWishId(Long wishId) {
        try (var st = connection.getStatement()) {
           // String sql;
            try (ResultSet rs = st.executeQuery(String.format("select * from bookings where wish_id =%d ", wishId))) {
                List<Booking> bookings = new ArrayList<>();
                while (rs.next()) {
                    bookings.add(Booking.builder().id(rs.getLong( "id"))
                            .userId(rs.getLong( "wishId"))
                            .userId(rs.getLong( "userId"))
                            .userId(rs.getLong( "ownerId")).build());
                }
                return bookings;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Booking> getByUserId(Long userId) {
        try (var st = connection.getStatement()) {
            String sql;
            try (ResultSet rs = st.executeQuery(String.format("select * from bookings where user_id =%d ", userId))) {
                List<Booking> bookings = new ArrayList<>();
                while (rs.next()) {
                    bookings.add(Booking.builder().id(rs.getLong( "id"))
                            .userId(rs.getLong( "wishId"))
                            .userId(rs.getLong( "userId"))
                            .userId(rs.getLong( "ownerId")).build());
                }
                return bookings;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public List<Booking> getByOwnerId(Long ownerId) {
        try (var st = connection.getStatement()) {
            // String sql;
            try (ResultSet rs = st.executeQuery(String.format("select * from bookings where owner_id =%d ", ownerId))) {
                List<Booking> bookings = new ArrayList<>();
                while (rs.next()) {
                    bookings.add(Booking.builder().id(rs.getLong( "id"))
                            .userId(rs.getLong( "wishId"))
                            .userId(rs.getLong( "userId"))
                            .userId(rs.getLong( "ownerId")).build());
                }
                return bookings;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void deleteByWishId(Long wishId) {
        try(var st=connection.getStatement()) {
            String q =  String.format("delete from bookings where wish_id=%d", wishId);
            //log.debug(q);
            st.execute(q);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
