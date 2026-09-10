package com.rupeek.hotelbooking.search;

import com.rupeek.hotelbooking.domain.Property;
import com.rupeek.hotelbooking.domain.RoomType;
import com.rupeek.hotelbooking.repository.PropertyRepository;
import com.rupeek.hotelbooking.service.AvailabilityService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchService {

    private final PropertyRepository propertyRepository;
    private final AvailabilityService availabilityService;
    private final List<PropertyFilter> filters;

    public SearchService(PropertyRepository propertyRepository,
                         AvailabilityService availabilityService,
                         List<PropertyFilter> filters) {
        this.propertyRepository = propertyRepository;
        this.availabilityService = availabilityService;
        this.filters = filters;
    }

    public List<Property> search(SearchCriteria criteria) {
        return propertyRepository.findAll().stream()
                .filter(property -> matchesAllFilters(property, criteria))
                .filter(property -> hasBookableRoomType(property, criteria))
                .collect(Collectors.toList());
    }

    private boolean matchesAllFilters(Property property, SearchCriteria criteria) {
        return filters.stream().allMatch(filter -> filter.matches(property, criteria));
    }

    private boolean hasBookableRoomType(Property property, SearchCriteria criteria) {
        return property.getRoomTypes().stream().anyMatch(roomType -> isBookable(property, roomType, criteria));
    }

    private boolean isBookable(Property property, RoomType roomType, SearchCriteria criteria) {
        return roomType.getCapacity() >= criteria.getGuests()
                && availabilityService.isAvailable(property.getId(), roomType,
                        criteria.getCheckIn(), criteria.getCheckOut());
    }
}
