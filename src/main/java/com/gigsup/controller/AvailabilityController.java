package com.gigsup.controller;

import com.gigsup.dto.AvailabilityRequest;
import com.gigsup.dto.AvailabilityResponse;
import com.gigsup.entity.Availability;
import com.gigsup.service.AvailabilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/availability")
@RequiredArgsConstructor
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    @PostMapping
    public AvailabilityResponse add(@Valid @RequestBody AvailabilityRequest req) {
        Availability availability = availabilityService.addAvailability(req);
        return AvailabilityResponse.from(availability);
    }

    @GetMapping
    public List<AvailabilityResponse> mine() {
        return availabilityService.myAvailability()
                .stream()
                .map(AvailabilityResponse::from)
                .toList();
    }
}
