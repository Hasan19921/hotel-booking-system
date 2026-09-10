package com.rupeek.hotelbooking.search;

import com.rupeek.hotelbooking.domain.Property;
import org.springframework.stereotype.Component;

@Component
public class StarRatingFilter implements PropertyFilter {

    @Override
    public boolean matches(Property property, SearchCriteria criteria) {
        if (criteria.getStarRating() == null) {
            return true;
        }
        return property.getStarRating() >= criteria.getStarRating();
    }
}
