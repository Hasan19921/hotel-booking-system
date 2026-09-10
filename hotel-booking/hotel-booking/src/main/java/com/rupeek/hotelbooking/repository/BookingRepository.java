package com.rupeek.hotelbooking.repository;

import com.rupeek.hotelbooking.domain.Booking;

import java.util.List;
import java.util.Optional;

public interface BookingRepository {

    Booking save(Booking booking);

    Optional<Booking> findById(Long id);

    List<Booking> findAll();
}
