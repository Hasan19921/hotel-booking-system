package com.rupeek.hotelbooking.search;

import com.rupeek.hotelbooking.domain.Booking;
import com.rupeek.hotelbooking.domain.BookingStatus;
import com.rupeek.hotelbooking.domain.Location;
import com.rupeek.hotelbooking.domain.Property;
import com.rupeek.hotelbooking.domain.RoomType;
import com.rupeek.hotelbooking.repository.BookingRepository;
import com.rupeek.hotelbooking.repository.InMemoryBookingRepository;
import com.rupeek.hotelbooking.repository.InMemoryPropertyRepository;
import com.rupeek.hotelbooking.repository.PropertyRepository;
import com.rupeek.hotelbooking.service.AvailabilityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SearchServiceTest {

    private static final LocalDate CHECK_IN = LocalDate.now().plusDays(5);
    private static final LocalDate CHECK_OUT = LocalDate.now().plusDays(7);

    private final PropertyRepository propertyRepository = new InMemoryPropertyRepository();
    private final BookingRepository bookingRepository = new InMemoryBookingRepository();
    private final AvailabilityService availabilityService = new AvailabilityService(bookingRepository);
    private final List<PropertyFilter> filters = List.of(
            new LocationFilter(), new PriceFilter(), new AmenityFilter(), new StarRatingFilter());
    private final SearchService searchService =
            new SearchService(propertyRepository, availabilityService, filters);

    private Property property;

    @BeforeEach
    void setUp() {
        RoomType singleRoomType = new RoomType(null, "Deluxe", 2, new BigDecimal("2000"), 1);
        property = propertyRepository.save(new Property("Rupeek Grand",
                new Location("Bangalore", "Indiranagar"), 4, Set.of("WIFI", "POOL"), List.of(singleRoomType)));
    }

    @Test
    void searchReturnsMatchingAvailableProperty() {
        SearchCriteria criteria = new SearchCriteria("bangalore", CHECK_IN, CHECK_OUT, 2,
                new BigDecimal("1000"), new BigDecimal("3000"), Set.of("WIFI"), 3);

        List<Property> results = searchService.search(criteria);

        assertEquals(1, results.size());
        assertEquals(property.getId(), results.get(0).getId());
    }

    @Test
    void unavailablePropertyIsNotReturned() {
        bookingRepository.save(new Booking(property.getId(), property.getRoomTypes().get(0).getId(),
                CHECK_IN, CHECK_OUT, 2, new BigDecimal("4000"), BookingStatus.CONFIRMED));

        SearchCriteria criteria = new SearchCriteria("Bangalore", CHECK_IN, CHECK_OUT, 2,
                null, null, null, null);

        assertTrue(searchService.search(criteria).isEmpty());
    }
}
