package com.rupeek.hotelbooking.search;

import com.rupeek.hotelbooking.domain.Property;
import org.springframework.stereotype.Component;

@Component
public class AmenityFilter implements PropertyFilter {

    @Override
    public boolean matches(Property property, SearchCriteria criteria) {
        if (criteria.getAmenities() == null || criteria.getAmenities().isEmpty()) {
            return true;
        }
        return property.getAmenities().containsAll(criteria.getAmenities());
    }
}
