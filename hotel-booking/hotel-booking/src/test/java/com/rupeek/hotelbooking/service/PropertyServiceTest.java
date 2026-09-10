package com.rupeek.hotelbooking.service;

import com.rupeek.hotelbooking.domain.Location;
import com.rupeek.hotelbooking.domain.Owner;
import com.rupeek.hotelbooking.domain.Property;
import com.rupeek.hotelbooking.domain.RoomType;
import com.rupeek.hotelbooking.exception.ResourceNotFoundException;
import com.rupeek.hotelbooking.repository.InMemoryOwnerRepository;
import com.rupeek.hotelbooking.repository.InMemoryPropertyRepository;
import com.rupeek.hotelbooking.repository.OwnerRepository;
import com.rupeek.hotelbooking.repository.PropertyRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PropertyServiceTest {

    private final OwnerRepository ownerRepository = new InMemoryOwnerRepository();
    private final PropertyRepository propertyRepository = new InMemoryPropertyRepository();
    private final OwnerService ownerService = new OwnerService(ownerRepository);
    private final PropertyService propertyService = new PropertyService(ownerRepository, propertyRepository);

    @Test
    void propertyCanBeAddedToOwner() {
        Owner owner = ownerService.createOwner("Rupeek Stays");

        Property saved = propertyService.addProperty(owner.getId(), newProperty());

        assertNotNull(saved.getId());
        assertEquals(owner.getId(), saved.getOwnerId());
        assertNotNull(saved.getRoomTypes().get(0).getId());
        assertEquals(1, ownerRepository.findById(owner.getId()).orElseThrow().getProperties().size());
        assertEquals(1, propertyRepository.findAll().size());
    }

    @Test
    void addingPropertyForMissingOwnerFails() {
        assertThrows(ResourceNotFoundException.class, () -> propertyService.addProperty(99L, newProperty()));
        assertEquals(0, propertyRepository.findAll().size());
    }

    private Property newProperty() {
        RoomType deluxe = new RoomType(null, "Deluxe", 2, new BigDecimal("2500"), 3);
        return new Property("Rupeek Grand", new Location("Bangalore", "Indiranagar"), 4,
                Set.of("WIFI", "POOL"), List.of(deluxe));
    }
}
