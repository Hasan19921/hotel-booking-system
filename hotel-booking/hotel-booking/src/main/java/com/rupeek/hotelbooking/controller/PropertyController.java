package com.rupeek.hotelbooking.controller;

import com.rupeek.hotelbooking.domain.Property;
import com.rupeek.hotelbooking.search.SearchCriteria;
import com.rupeek.hotelbooking.search.SearchService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/properties")
public class PropertyController {

    private final SearchService searchService;

    public PropertyController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/search")
    public List<Property> search(
            @RequestParam String city,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut,
            @RequestParam int guests,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Set<String> amenities,
            @RequestParam(required = false) Integer starRating) {

        SearchCriteria criteria = new SearchCriteria(city, checkIn, checkOut, guests,
                minPrice, maxPrice, amenities, starRating);
        return searchService.search(criteria);
    }
}
