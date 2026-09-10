package com.rupeek.hotelbooking.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Booking {

    private Long id;
    private Long propertyId;
    private Long roomTypeId;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private int guests;
    private BigDecimal amount;
    private BookingStatus status;

    public Booking() {
    }

    public Booking(Long propertyId, Long roomTypeId, LocalDate checkIn, LocalDate checkOut,
                   int guests, BigDecimal amount, BookingStatus status) {
        this.propertyId = propertyId;
        this.roomTypeId = roomTypeId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.guests = guests;
        this.amount = amount;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }

    public Long getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(Long roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
    }

    public int getGuests() {
        return guests;
    }

    public void setGuests(int guests) {
        this.guests = guests;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }
}
