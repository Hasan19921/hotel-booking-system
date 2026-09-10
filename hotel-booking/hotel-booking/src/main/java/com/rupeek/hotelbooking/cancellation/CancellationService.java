package com.rupeek.hotelbooking.cancellation;

import com.rupeek.hotelbooking.domain.Booking;
import com.rupeek.hotelbooking.domain.BookingStatus;
import com.rupeek.hotelbooking.exception.InvalidBookingStateException;
import com.rupeek.hotelbooking.exception.ResourceNotFoundException;
import com.rupeek.hotelbooking.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class CancellationService {

    private final BookingRepository bookingRepository;
    private final CancellationPolicy cancellationPolicy;

    public CancellationService(BookingRepository bookingRepository, CancellationPolicy cancellationPolicy) {
        this.bookingRepository = bookingRepository;
        this.cancellationPolicy = cancellationPolicy;
    }

    public CancellationResult cancel(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + bookingId));

        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new InvalidBookingStateException(
                    "Only a CONFIRMED booking can be cancelled, current state: " + booking.getStatus());
        }

        BigDecimal refundedAmount = cancellationPolicy.calculateRefund(booking, LocalDateTime.now());

        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);

        return new CancellationResult(booking.getId(), refundedAmount, booking.getStatus());
    }
}
