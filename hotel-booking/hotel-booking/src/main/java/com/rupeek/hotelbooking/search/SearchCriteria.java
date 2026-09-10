package com.rupeek.hotelbooking.search;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

public class SearchCriteria {

    private final String city;
    private final LocalDate checkIn;
    private final LocalDate checkOut;
    private final int guests;
    private final BigDecimal minPrice;
    private final BigDecimal maxPrice;
    private final Set<String> amenities;
    private final Integer starRating;

    public SearchCriteria(String city, LocalDate checkIn, LocalDate checkOut, int guests,
                          BigDecimal minPrice, BigDecimal maxPrice,
                          Set<String> amenities, Integer starRating) {
        this.city = city;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.guests = guests;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.amenities = amenities;
        this.starRating = starRating;
    }

    public String getCity() {
        return city;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public int getGuests() {
        return guests;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public Set<String> getAmenities() {
        return amenities;
    }

    public Integer getStarRating() {
        return starRating;
    }
}
