
package com.gigsup.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;

@Data
public class AvailabilityRequest {

    @NotNull
    @Future
    private Instant availableFrom;

    @NotNull
    @Future
    private Instant availableTo;
}
