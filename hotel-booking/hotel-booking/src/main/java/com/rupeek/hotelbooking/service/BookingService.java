package com.rupeek.hotelbooking.service;

import com.rupeek.hotelbooking.domain.Booking;
import com.rupeek.hotelbooking.domain.BookingStatus;
import com.rupeek.hotelbooking.domain.Property;
import com.rupeek.hotelbooking.domain.RoomType;
import com.rupeek.hotelbooking.exception.InvalidBookingException;
import com.rupeek.hotelbooking.exception.ResourceNotFoundException;
import com.rupeek.hotelbooking.exception.RoomNotAvailableException;
import com.rupeek.hotelbooking.repository.BookingRepository;
import com.rupeek.hotelbooking.repository.PropertyRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class BookingService {

    private final PropertyRepository propertyRepository;
    private final BookingRepository bookingRepository;
    private final AvailabilityService availabilityService;

    public BookingService(PropertyRepository propertyRepository,
                          BookingRepository bookingRepository,
                          AvailabilityService availabilityService) {
        this.propertyRepository = propertyRepository;
        this.bookingRepository = bookingRepository;
        this.availabilityService = availabilityService;
    }

    public Booking createBooking(Long propertyId, Long roomTypeId, LocalDate checkIn,
                                 LocalDate checkOut, int guests) {
        validateDates(checkIn, checkOut);

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found: " + propertyId));
        RoomType roomType = findRoomType(property, roomTypeId);

        validateGuests(guests, roomType);

        if (!availabilityService.isAvailable(propertyId, roomType, checkIn, checkOut)) {
            throw new RoomNotAvailableException(
                    "No rooms available for room type " + roomTypeId + " on the requested dates");
        }

        BigDecimal amount = calculateAmount(roomType, checkIn, checkOut);
        return bookingRepository.save(new Booking(propertyId, roomType.getId(), checkIn, checkOut,
                guests, amount, BookingStatus.CREATED));
    }

    private RoomType findRoomType(Property property, Long roomTypeId) {
        return property.getRoomTypes().stream()
                .filter(roomType -> roomTypeId != null && roomTypeId.equals(roomType.getId()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Room type " + roomTypeId + " not found in property " + property.getId()));
    }

    private void validateDates(LocalDate checkIn, LocalDate checkOut) {
        if (checkIn == null) {
            throw new InvalidBookingException("checkIn is required");
        }
        if (checkOut == null) {
            throw new InvalidBookingException("checkOut is required");
        }
        if (!checkOut.isAfter(checkIn)) {
            throw new InvalidBookingException("checkOut must be after checkIn");
        }
    }

    private void validateGuests(int guests, RoomType roomType) {
        if (guests <= 0) {
            throw new InvalidBookingException("guests must be greater than 0");
        }
        if (guests > roomType.getCapacity()) {
            throw new InvalidBookingException(
                    "guests exceed room type capacity of " + roomType.getCapacity());
        }
    }

    private BigDecimal calculateAmount(RoomType roomType, LocalDate checkIn, LocalDate checkOut) {
        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        return roomType.getPricePerNight().multiply(BigDecimal.valueOf(nights));
    }
}
