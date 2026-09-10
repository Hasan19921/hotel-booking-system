package com.rupeek.hotelbooking.cancellation;

import com.rupeek.hotelbooking.domain.BookingStatus;

import java.math.BigDecimal;

public class CancellationResult {

    private final Long bookingId;
    private final BigDecimal refundedAmount;
    private final BookingStatus status;

    public CancellationResult(Long bookingId, BigDecimal refundedAmount, BookingStatus status) {
        this.bookingId = bookingId;
        this.refundedAmount = refundedAmount;
        this.status = status;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public BigDecimal getRefundedAmount() {
        return refundedAmount;
    }

    public BookingStatus getStatus() {
        return status;
    }
}
