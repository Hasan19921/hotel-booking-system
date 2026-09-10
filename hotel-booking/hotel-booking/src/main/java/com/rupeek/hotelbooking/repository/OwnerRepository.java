package com.rupeek.hotelbooking.repository;

import com.rupeek.hotelbooking.domain.Owner;

import java.util.Optional;

public interface OwnerRepository {

    Owner save(Owner owner);

    Optional<Owner> findById(Long id);
}
