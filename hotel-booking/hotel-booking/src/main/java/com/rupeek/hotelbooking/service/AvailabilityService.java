package com.rupeek.hotelbooking.service;

import com.rupeek.hotelbooking.domain.Booking;
import com.rupeek.hotelbooking.domain.BookingStatus;
import com.rupeek.hotelbooking.domain.RoomType;
import com.rupeek.hotelbooking.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.EnumSet;
import java.util.Set;

@Service
public class AvailabilityService {

    private static final Set<BookingStatus> INVENTORY_CONSUMING_STATUSES =
            EnumSet.of(BookingStatus.CREATED, BookingStatus.PAYMENT_PENDING, BookingStatus.CONFIRMED);

    private final BookingRepository bookingRepository;

    public AvailabilityService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public boolean isAvailable(Long propertyId, RoomType roomType, LocalDate checkIn, LocalDate checkOut) {
        return countOccupiedRooms(propertyId, roomType.getId(), checkIn, checkOut) < roomType.getTotalRooms();
    }

    private long countOccupiedRooms(Long propertyId, Long roomTypeId, LocalDate checkIn, LocalDate checkOut) {
        return bookingRepository.findAll().stream()
                .filter(booking -> propertyId.equals(booking.getPropertyId()))
                .filter(booking -> roomTypeId.equals(booking.getRoomTypeId()))
                .filter(booking -> INVENTORY_CONSUMING_STATUSES.contains(booking.getStatus()))
                .filter(booking -> overlaps(booking, checkIn, checkOut))
                .count();
    }

    private boolean overlaps(Booking booking, LocalDate requestedCheckIn, LocalDate requestedCheckOut) {
        return booking.getCheckIn().isBefore(requestedCheckOut)
                && requestedCheckIn.isBefore(booking.getCheckOut());
    }
}
