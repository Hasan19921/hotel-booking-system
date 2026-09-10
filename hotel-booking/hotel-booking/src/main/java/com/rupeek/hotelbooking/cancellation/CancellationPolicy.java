package com.rupeek.hotelbooking.cancellation;

import com.rupeek.hotelbooking.domain.Booking;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface CancellationPolicy {

    BigDecimal calculateRefund(Booking booking, LocalDateTime cancellationTime);
}
