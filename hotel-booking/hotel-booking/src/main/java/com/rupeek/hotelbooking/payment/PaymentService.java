package com.rupeek.hotelbooking.payment;

import com.rupeek.hotelbooking.domain.Booking;
import com.rupeek.hotelbooking.domain.BookingStatus;
import com.rupeek.hotelbooking.exception.InvalidBookingStateException;
import com.rupeek.hotelbooking.exception.PaymentException;
import com.rupeek.hotelbooking.exception.ResourceNotFoundException;
import com.rupeek.hotelbooking.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
public class PaymentService {

    private final BookingRepository bookingRepository;
    private final Map<PaymentMethod, PaymentProcessor> processors = new EnumMap<>(PaymentMethod.class);

    public PaymentService(BookingRepository bookingRepository, List<PaymentProcessor> paymentProcessors) {
        this.bookingRepository = bookingRepository;
        for (PaymentProcessor processor : paymentProcessors) {
            this.processors.put(processor.supportedMethod(), processor);
        }
    }

    public Booking pay(Long bookingId, PaymentMethod paymentMethod) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + bookingId));

        validatePayable(booking);
        PaymentProcessor processor = resolveProcessor(paymentMethod);

        booking.setStatus(BookingStatus.PAYMENT_PENDING);
        bookingRepository.save(booking);

        PaymentResult result = processor.process(booking);
        booking.setStatus(result.isSuccess() ? BookingStatus.CONFIRMED : BookingStatus.PAYMENT_FAILED);
        return bookingRepository.save(booking);
    }

    private void validatePayable(Booking booking) {
        if (booking.getStatus() != BookingStatus.CREATED
                && booking.getStatus() != BookingStatus.PAYMENT_FAILED) {
            throw new InvalidBookingStateException(
                    "Booking in state " + booking.getStatus() + " cannot be paid");
        }
    }

    private PaymentProcessor resolveProcessor(PaymentMethod paymentMethod) {
        PaymentProcessor processor = processors.get(paymentMethod);
        if (processor == null) {
            throw new PaymentException("Unsupported payment method: " + paymentMethod);
        }
        return processor;
    }
}
