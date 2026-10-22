package com.gigsup.dto;

import com.gigsup.entity.Availability;
import lombok.Data;

import java.time.Instant;

@Data
public class AvailabilityResponse {
    private Long id;
    private Instant availableFrom;
    private Instant availableTo;
    private Long studentId;

    public static AvailabilityResponse from(Availability a) {
        AvailabilityResponse r = new AvailabilityResponse();
        r.setId(a.getId());
        r.setAvailableFrom(a.getAvailableFrom());
        r.setAvailableTo(a.getAvailableTo());
        r.setStudentId(a.getStudent().getId());
        return r;
    }
}
