package com.rupeek.hotelbooking.controller;

import com.rupeek.hotelbooking.cancellation.CancellationResult;
import com.rupeek.hotelbooking.cancellation.CancellationService;
import com.rupeek.hotelbooking.domain.Booking;
import com.rupeek.hotelbooking.dto.BookingRequest;
import com.rupeek.hotelbooking.dto.PaymentRequest;
import com.rupeek.hotelbooking.payment.PaymentService;
import com.rupeek.hotelbooking.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final PaymentService paymentService;
    private final CancellationService cancellationService;

    public BookingController(BookingService bookingService,
                             PaymentService paymentService,
                             CancellationService cancellationService) {
        this.bookingService = bookingService;
        this.paymentService = paymentService;
        this.cancellationService = cancellationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Booking createBooking(@RequestBody BookingRequest request) {
        return bookingService.createBooking(
                request.getPropertyId(),
                request.getRoomTypeId(),
                request.getCheckIn(),
                request.getCheckOut(),
                request.getGuests());
    }

    @PostMapping("/{bookingId}/payments")
    public Booking pay(@PathVariable Long bookingId, @RequestBody PaymentRequest request) {
        return paymentService.pay(bookingId, request.getPaymentMethod());
    }

    @PostMapping("/{bookingId}/cancel")
    public CancellationResult cancel(@PathVariable Long bookingId) {
        return cancellationService.cancel(bookingId);
    }
}
