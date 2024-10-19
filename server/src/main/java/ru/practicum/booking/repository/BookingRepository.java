package ru.practicum.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.booking.model.Booking;
import ru.practicum.booking.status.BookingStatus;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findBookingsByBooker_IdOrderByStartDesc(long bookerId);

    @Query("SELECT b FROM Booking  b " +
            "WHERE b.booker.id = :bookerId " +
            "AND b.start <= CURRENT_TIMESTAMP " +
            "AND b.end >= CURRENT_TIMESTAMP " +
            "ORDER BY b.start DESC")
    List<Booking> findActiveBookingsByBookerId(long bookerId);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = :bookerId " +
            "AND b.end <= CURRENT_TIMESTAMP " +
            "ORDER BY b.start DESC")
    List<Booking> findPastBookingsByBookerId(long bookerId);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = :bookerId " +
            "AND b.end <= CURRENT_TIMESTAMP " +
            "ORDER BY b.start DESC")
    List<Booking> findFutureBookingsByBookerId(long bookerId);

    List<Booking> findBookingsByBooker_IdAndStatusOrderByStartDesc(long bookerId, BookingStatus status);

    @Query("SELECT (COUNT(b) > 0) FROM Booking b " +
            "WHERE b.booker.id = :bookerId " +
            "AND b.item.id = :itemId " +
            "AND b.end <= CURRENT_TIMESTAMP")
    boolean existsByBookerIdAndItemIdPast(long bookerId, long itemId);

    boolean existsByItemId(long itemId);
}