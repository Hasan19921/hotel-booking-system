package com.rupeek.hotelbooking.payment;

import com.rupeek.hotelbooking.domain.Booking;
import com.rupeek.hotelbooking.domain.BookingStatus;
import com.rupeek.hotelbooking.exception.InvalidBookingStateException;
import com.rupeek.hotelbooking.repository.BookingRepository;
import com.rupeek.hotelbooking.repository.InMemoryBookingRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PaymentServiceTest {

    private final BookingRepository bookingRepository = new InMemoryBookingRepository();

    @Test
    void successfulPaymentConfirmsBooking() {
        PaymentService paymentService = new PaymentService(bookingRepository,
                List.of(new UpiPaymentProcessor()));
        Booking booking = savedBooking(BookingStatus.CREATED);

        Booking paid = paymentService.pay(booking.getId(), PaymentMethod.UPI);

        assertEquals(BookingStatus.CONFIRMED, paid.getStatus());
        assertEquals(BookingStatus.CONFIRMED,
                bookingRepository.findById(booking.getId()).orElseThrow().getStatus());
    }

    @Test
    void failedPaymentMarksBookingPaymentFailed() {
        PaymentProcessor failingProcessor = mock(PaymentProcessor.class);
        when(failingProcessor.supportedMethod()).thenReturn(PaymentMethod.CARD);
        when(failingProcessor.process(any(Booking.class))).thenReturn(new PaymentResult(false, null));

        PaymentService paymentService = new PaymentService(bookingRepository, List.of(failingProcessor));
        Booking booking = savedBooking(BookingStatus.CREATED);

        Booking paid = paymentService.pay(booking.getId(), PaymentMethod.CARD);

        assertEquals(BookingStatus.PAYMENT_FAILED, paid.getStatus());
    }

    @Test
    void cancelledBookingCannotBePaid() {
        PaymentService paymentService = new PaymentService(bookingRepository,
                List.of(new WalletPaymentProcessor()));
        Booking booking = savedBooking(BookingStatus.CANCELLED);

        assertThrows(InvalidBookingStateException.class,
                () -> paymentService.pay(booking.getId(), PaymentMethod.WALLET));
        assertEquals(BookingStatus.CANCELLED,
                bookingRepository.findById(booking.getId()).orElseThrow().getStatus());
    }

    private Booking savedBooking(BookingStatus status) {
        return bookingRepository.save(new Booking(1L, 1L,
                LocalDate.now().plusDays(10), LocalDate.now().plusDays(12), 2,
                new BigDecimal("4000"), status));
    }
}
