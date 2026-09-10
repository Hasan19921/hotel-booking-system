package com.rupeek.hotelbooking.service;

import com.rupeek.hotelbooking.domain.Booking;
import com.rupeek.hotelbooking.domain.BookingStatus;
import com.rupeek.hotelbooking.domain.Location;
import com.rupeek.hotelbooking.domain.Property;
import com.rupeek.hotelbooking.domain.RoomType;
import com.rupeek.hotelbooking.exception.InvalidBookingException;
import com.rupeek.hotelbooking.exception.RoomNotAvailableException;
import com.rupeek.hotelbooking.repository.BookingRepository;
import com.rupeek.hotelbooking.repository.InMemoryBookingRepository;
import com.rupeek.hotelbooking.repository.InMemoryPropertyRepository;
import com.rupeek.hotelbooking.repository.PropertyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BookingServiceTest {

    private static final LocalDate CHECK_IN = LocalDate.now().plusDays(10);
    private static final LocalDate CHECK_OUT = LocalDate.now().plusDays(12);

    private final PropertyRepository propertyRepository = new InMemoryPropertyRepository();
    private final BookingRepository bookingRepository = new InMemoryBookingRepository();
    private final AvailabilityService availabilityService = new AvailabilityService(bookingRepository);
    private final BookingService bookingService =
            new BookingService(propertyRepository, bookingRepository, availabilityService);

    private Property property;

    @BeforeEach
    void setUp() {
        RoomType singleRoomType = new RoomType(null, "Deluxe", 2, new BigDecimal("2000"), 1);
        property = propertyRepository.save(new Property("Rupeek Grand",
                new Location("Bangalore", "Indiranagar"), 4, Set.of("WIFI"), List.of(singleRoomType)));
    }

    @Test
    void bookingSucceedsWhenRoomAvailable() {
        Booking booking = bookingService.createBooking(property.getId(), roomTypeId(),
                CHECK_IN, CHECK_OUT, 2);

        assertNotNull(booking.getId());
        assertEquals(BookingStatus.CREATED, booking.getStatus());
        assertEquals(0, new BigDecimal("4000").compareTo(booking.getAmount()));
    }

    @Test
    void bookingFailsWhenInventoryIsFull() {
        bookingService.createBooking(property.getId(), roomTypeId(), CHECK_IN, CHECK_OUT, 2);

        assertThrows(RoomNotAvailableException.class, () -> bookingService.createBooking(
                property.getId(), roomTypeId(), CHECK_IN, CHECK_OUT, 2));
    }

    @Test
    void overlappingBookingIsPrevented() {
        bookingService.createBooking(property.getId(), roomTypeId(), CHECK_IN, CHECK_OUT, 2);

        assertThrows(RoomNotAvailableException.class, () -> bookingService.createBooking(
                property.getId(), roomTypeId(), CHECK_IN.plusDays(1), CHECK_OUT.plusDays(1), 2));
    }

    @Test
    void invalidDateRangeIsRejected() {
        assertThrows(InvalidBookingException.class, () -> bookingService.createBooking(
                property.getId(), roomTypeId(), CHECK_OUT, CHECK_IN, 2));
    }

    private Long roomTypeId() {
        return property.getRoomTypes().get(0).getId();
    }
}
