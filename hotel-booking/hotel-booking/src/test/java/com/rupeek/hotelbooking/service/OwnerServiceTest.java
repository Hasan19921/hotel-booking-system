package com.rupeek.hotelbooking.service;

import com.rupeek.hotelbooking.domain.Owner;
import com.rupeek.hotelbooking.repository.InMemoryOwnerRepository;
import com.rupeek.hotelbooking.repository.OwnerRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OwnerServiceTest {

    private final OwnerRepository ownerRepository = new InMemoryOwnerRepository();
    private final OwnerService ownerService = new OwnerService(ownerRepository);

    @Test
    void ownerCanBeCreated() {
        Owner owner = ownerService.createOwner("Rupeek Stays");

        assertNotNull(owner.getId());
        assertEquals("Rupeek Stays", owner.getName());
        assertTrue(owner.getProperties().isEmpty());
        assertTrue(ownerRepository.findById(owner.getId()).isPresent());
    }
}
