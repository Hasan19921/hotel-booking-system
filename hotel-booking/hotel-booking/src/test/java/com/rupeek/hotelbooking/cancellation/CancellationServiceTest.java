package com.rupeek.hotelbooking.cancellation;

import com.rupeek.hotelbooking.domain.Booking;
import com.rupeek.hotelbooking.domain.BookingStatus;
import com.rupeek.hotelbooking.domain.RoomType;
import com.rupeek.hotelbooking.repository.BookingRepository;
import com.rupeek.hotelbooking.repository.InMemoryBookingRepository;
import com.rupeek.hotelbooking.service.AvailabilityService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CancellationServiceTest {

    private static final LocalDate CHECK_IN = LocalDate.now().plusDays(10);
    private static final LocalDate CHECK_OUT = LocalDate.now().plusDays(12);

    private final BookingRepository bookingRepository = new InMemoryBookingRepository();
    private final AvailabilityService availabilityService = new AvailabilityService(bookingRepository);
    private final CancellationService cancellationService =
            new CancellationService(bookingRepository, new StandardCancellationPolicy());

    @Test
    void confirmedBookingCanBeCancelled() {
        Booking booking = confirmedBooking();

        CancellationResult result = cancellationService.cancel(booking.getId());

        assertEquals(booking.getId(), result.getBookingId());
        assertEquals(BookingStatus.CANCELLED, result.getStatus());
        assertEquals(0, new BigDecimal("4000").compareTo(result.getRefundedAmount()));
        assertEquals(BookingStatus.CANCELLED,
                bookingRepository.findById(booking.getId()).orElseThrow().getStatus());
    }

    @Test
    void cancellationReleasesInventory() {
        Booking booking = confirmedBooking();
        RoomType roomType = new RoomType(1L, "Deluxe", 2, new BigDecimal("2000"), 1);

        assertFalse(availabilityService.isAvailable(1L, roomType, CHECK_IN, CHECK_OUT));

        cancellationService.cancel(booking.getId());

        assertTrue(availabilityService.isAvailable(1L, roomType, CHECK_IN, CHECK_OUT));
    }

    private Booking confirmedBooking() {
        return bookingRepository.save(new Booking(1L, 1L, CHECK_IN, CHECK_OUT, 2,
                new BigDecimal("4000"), BookingStatus.CONFIRMED));
    }
}
