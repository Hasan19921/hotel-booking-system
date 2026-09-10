package com.rupeek.hotelbooking.search;

import com.rupeek.hotelbooking.domain.Property;
import com.rupeek.hotelbooking.domain.RoomType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PriceFilter implements PropertyFilter {

    @Override
    public boolean matches(Property property, SearchCriteria criteria) {
        if (criteria.getMinPrice() == null && criteria.getMaxPrice() == null) {
            return true;
        }
        return property.getRoomTypes().stream().anyMatch(roomType -> withinRange(roomType, criteria));
    }

    private boolean withinRange(RoomType roomType, SearchCriteria criteria) {
        BigDecimal price = roomType.getPricePerNight();
        if (price == null) {
            return false;
        }
        if (criteria.getMinPrice() != null && price.compareTo(criteria.getMinPrice()) < 0) {
            return false;
        }
        return criteria.getMaxPrice() == null || price.compareTo(criteria.getMaxPrice()) <= 0;
    }
}
