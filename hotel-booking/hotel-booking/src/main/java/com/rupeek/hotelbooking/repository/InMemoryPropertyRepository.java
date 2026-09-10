package com.rupeek.hotelbooking.repository;

import com.rupeek.hotelbooking.domain.Property;
import com.rupeek.hotelbooking.domain.RoomType;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryPropertyRepository implements PropertyRepository {

    private final Map<Long, Property> properties = new ConcurrentHashMap<>();
    private final AtomicLong propertyIdSequence = new AtomicLong();
    private final AtomicLong roomTypeIdSequence = new AtomicLong();

    @Override
    public Property save(Property property) {
        if (property.getId() == null) {
            property.setId(propertyIdSequence.incrementAndGet());
        }
        assignRoomTypeIds(property);
        properties.put(property.getId(), property);
        return property;
    }

    @Override
    public Optional<Property> findById(Long id) {
        return Optional.ofNullable(properties.get(id));
    }

    @Override
    public List<Property> findAll() {
        return new ArrayList<>(properties.values());
    }

    private void assignRoomTypeIds(Property property) {
        for (RoomType roomType : property.getRoomTypes()) {
            if (roomType.getId() == null) {
                roomType.setId(roomTypeIdSequence.incrementAndGet());
            }
        }
    }
}
