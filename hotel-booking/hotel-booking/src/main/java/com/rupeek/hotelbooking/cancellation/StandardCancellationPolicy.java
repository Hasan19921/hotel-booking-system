package com.rupeek.hotelbooking.cancellation;

import com.rupeek.hotelbooking.domain.Booking;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class StandardCancellationPolicy implements CancellationPolicy {

    private static final int FREE_CANCELLATION_WINDOW_HOURS = 24;

    @Override
    public BigDecimal calculateRefund(Booking booking, LocalDateTime cancellationTime) {
        LocalDateTime cutoff = booking.getCheckIn().atStartOfDay()
                .minusHours(FREE_CANCELLATION_WINDOW_HOURS);
        if (cancellationTime.isAfter(cutoff)) {
            return BigDecimal.ZERO;
        }
        return booking.getAmount();
    }
}
