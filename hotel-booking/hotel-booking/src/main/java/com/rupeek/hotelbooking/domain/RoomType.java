package com.rupeek.hotelbooking.domain;

import java.math.BigDecimal;

public class RoomType {

    private Long id;
    private String name;
    private int capacity;
    private BigDecimal pricePerNight;
    private int totalRooms;

    public RoomType() {
    }

    public RoomType(Long id, String name, int capacity, BigDecimal pricePerNight, int totalRooms) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.pricePerNight = pricePerNight;
        this.totalRooms = totalRooms;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public BigDecimal getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(BigDecimal pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public int getTotalRooms() {
        return totalRooms;
    }

    public void setTotalRooms(int totalRooms) {
        this.totalRooms = totalRooms;
    }
}
