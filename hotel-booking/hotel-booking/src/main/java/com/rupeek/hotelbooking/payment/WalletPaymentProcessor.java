package com.rupeek.hotelbooking.payment;

import com.rupeek.hotelbooking.domain.Booking;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class WalletPaymentProcessor implements PaymentProcessor {

    @Override
    public PaymentMethod supportedMethod() {
        return PaymentMethod.WALLET;
    }

    @Override
    public PaymentResult process(Booking booking) {
        return new PaymentResult(true, "WALLET-" + UUID.randomUUID());
    }
}
