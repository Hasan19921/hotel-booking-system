package com.rupeek.hotelbooking.repository;

import com.rupeek.hotelbooking.domain.Property;

import java.util.List;
import java.util.Optional;

public interface PropertyRepository {

    Property save(Property property);

    Optional<Property> findById(Long id);

    List<Property> findAll();
}
