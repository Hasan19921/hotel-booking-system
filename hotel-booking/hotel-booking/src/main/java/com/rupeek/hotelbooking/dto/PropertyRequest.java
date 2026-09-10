package com.rupeek.hotelbooking.dto;

import com.rupeek.hotelbooking.domain.Location;
import com.rupeek.hotelbooking.domain.Property;
import com.rupeek.hotelbooking.domain.RoomType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PropertyRequest {

    private String name;
    private String city;
    private String locality;
    private int starRating;
    private Set<String> amenities = new HashSet<>();
    private List<RoomTypeRequest> roomTypes = new ArrayList<>();

    public Property toProperty() {
        List<RoomType> types = new ArrayList<>();
        for (RoomTypeRequest request : roomTypes) {
            types.add(request.toRoomType());
        }
        return new Property(name, new Location(city, locality), starRating, amenities, types);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
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

    public List<RoomTypeRequest> getRoomTypes() {
        return roomTypes;
    }

    public void setRoomTypes(List<RoomTypeRequest> roomTypes) {
        this.roomTypes = roomTypes;
    }

    public static class RoomTypeRequest {

        private String name;
        private int capacity;
        private BigDecimal pricePerNight;
        private int totalRooms;

        public RoomType toRoomType() {
            return new RoomType(null, name, capacity, pricePerNight, totalRooms);
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
}
