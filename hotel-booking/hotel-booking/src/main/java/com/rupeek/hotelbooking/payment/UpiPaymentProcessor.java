package com.rupeek.hotelbooking.payment;

import com.rupeek.hotelbooking.domain.Booking;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UpiPaymentProcessor implements PaymentProcessor {

    @Override
    public PaymentMethod supportedMethod() {
        return PaymentMethod.UPI;
    }

    @Override
    public PaymentResult process(Booking booking) {
        return new PaymentResult(true, "UPI-" + UUID.randomUUID());
    }
}
