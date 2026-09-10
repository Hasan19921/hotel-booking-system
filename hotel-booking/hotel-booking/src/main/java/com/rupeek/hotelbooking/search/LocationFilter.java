package com.rupeek.hotelbooking.search;

import com.rupeek.hotelbooking.domain.Property;
import org.springframework.stereotype.Component;

@Component
public class LocationFilter implements PropertyFilter {

    @Override
    public boolean matches(Property property, SearchCriteria criteria) {
        if (criteria.getCity() == null) {
            return true;
        }
        if (property.getLocation() == null || property.getLocation().getCity() == null) {
            return false;
        }
        return property.getLocation().getCity().equalsIgnoreCase(criteria.getCity());
    }
}
