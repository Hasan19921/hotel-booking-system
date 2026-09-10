package com.rupeek.hotelbooking.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Property {

    private Long id;
    private Long ownerId;
    private String name;
    private Location location;
    private int starRating;
    private Set<String> amenities = new HashSet<>();
    private List<RoomType> roomTypes = new ArrayList<>();

    public Property() {
    }

    public Property(String name, Location location, int starRating,
                    Set<String> amenities, List<RoomType> roomTypes) {
        this.name = name;
        this.location = location;
        this.starRating = starRating;
        if (amenities != null) {
            this.amenities = amenities;
        }
        if (roomTypes != null) {
            this.roomTypes = roomTypes;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getStarRating() {
        return starRating;
    }

    public void setStarRating(int starRating) {
        this.starRating = starRating;
    }

    public Set<String> getAmenities() {
        return amenities;
    }

    public void setAmenities(Set<String> amenities) {
        this.amenities = amenities;
    }

    public List<RoomType> getRoomTypes() {
        return roomTypes;
    }

    public void setRoomTypes(List<RoomType> roomTypes) {
        this.roomTypes = roomTypes;
    }
}
