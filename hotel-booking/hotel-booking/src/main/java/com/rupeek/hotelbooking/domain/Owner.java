package com.rupeek.hotelbooking.domain;

import java.util.ArrayList;
import java.util.List;

public class Owner {

    private Long id;
    private String name;
    private List<Property> properties = new ArrayList<>();

    public Owner() {
    }

    public Owner(String name) {
        this.name = name;
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

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    public void addProperty(Property property) {
        this.properties.add(property);
    }
}
