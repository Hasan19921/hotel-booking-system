package com.rupeek.hotelbooking.service;

import com.rupeek.hotelbooking.domain.Owner;
import com.rupeek.hotelbooking.repository.OwnerRepository;
import org.springframework.stereotype.Service;

@Service
public class OwnerService {

    private final OwnerRepository ownerRepository;

    public OwnerService(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    public Owner createOwner(String name) {
        return ownerRepository.save(new Owner(name));
    }
}
