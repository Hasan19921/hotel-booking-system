package com.rupeek.hotelbooking.dto;

import com.rupeek.hotelbooking.payment.PaymentMethod;

public class PaymentRequest {

    private PaymentMethod paymentMethod;

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
