package com.rupeek.hotelbooking.service;

import com.rupeek.hotelbooking.domain.Owner;
import com.rupeek.hotelbooking.domain.Property;
import com.rupeek.hotelbooking.exception.ResourceNotFoundException;
import com.rupeek.hotelbooking.repository.OwnerRepository;
import com.rupeek.hotelbooking.repository.PropertyRepository;
import org.springframework.stereotype.Service;

@Service
public class PropertyService {

    private final OwnerRepository ownerRepository;
    private final PropertyRepository propertyRepository;

    public PropertyService(OwnerRepository ownerRepository, PropertyRepository propertyRepository) {
        this.ownerRepository = ownerRepository;
        this.propertyRepository = propertyRepository;
    }

    public Property addProperty(Long ownerId, Property property) {
        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found: " + ownerId));

        property.setOwnerId(owner.getId());
        Property savedProperty = propertyRepository.save(property);

        owner.addProperty(savedProperty);
        ownerRepository.save(owner);

        return savedProperty;
    }
}
