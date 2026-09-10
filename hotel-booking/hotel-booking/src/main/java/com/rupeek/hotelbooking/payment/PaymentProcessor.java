package com.rupeek.hotelbooking.payment;

import com.rupeek.hotelbooking.domain.Booking;

public interface PaymentProcessor {

    PaymentMethod supportedMethod();

    PaymentResult process(Booking booking);
}
