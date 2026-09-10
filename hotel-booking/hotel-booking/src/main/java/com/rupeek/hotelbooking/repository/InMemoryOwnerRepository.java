package com.rupeek.hotelbooking.repository;

import com.rupeek.hotelbooking.domain.Owner;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryOwnerRepository implements OwnerRepository {

    private final Map<Long, Owner> owners = new ConcurrentHashMap<>();
    private final AtomicLong idSequence = new AtomicLong();

    @Override
    public Owner save(Owner owner) {
        if (owner.getId() == null) {
            owner.setId(idSequence.incrementAndGet());
        }
        owners.put(owner.getId(), owner);
        return owner;
    }

    @Override
    public Optional<Owner> findById(Long id) {
        return Optional.ofNullable(owners.get(id));
    }
}
