package com.rupeek.hotelbooking.controller;

import com.rupeek.hotelbooking.domain.Owner;
import com.rupeek.hotelbooking.domain.Property;
import com.rupeek.hotelbooking.dto.CreateOwnerRequest;
import com.rupeek.hotelbooking.dto.PropertyRequest;
import com.rupeek.hotelbooking.service.OwnerService;
import com.rupeek.hotelbooking.service.PropertyService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/owners")
public class OwnerController {

    private final OwnerService ownerService;
    private final PropertyService propertyService;

    public OwnerController(OwnerService ownerService, PropertyService propertyService) {
        this.ownerService = ownerService;
        this.propertyService = propertyService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Owner createOwner(@RequestBody CreateOwnerRequest request) {
        return ownerService.createOwner(request.getName());
    }

    @PostMapping("/{ownerId}/properties")
    @ResponseStatus(HttpStatus.CREATED)
    public Property addProperty(@PathVariable Long ownerId, @RequestBody PropertyRequest request) {
        return propertyService.addProperty(ownerId, request.toProperty());
    }
}
