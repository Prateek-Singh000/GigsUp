
package com.gigsup.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class ShiftRequest {

    @NotBlank(message = "Shift title is required")
    private String title;

    private String description;

    @NotNull
    @DecimalMin(value = "0.01", message = "Pay rate must be greater than zero")
    private BigDecimal payRate;

    @NotNull
    @DecimalMin(value = "-90.0")
    @DecimalMax(value = "90.0")
    private Double lat;

    @NotNull
    @DecimalMin(value = "-180.0")
    @DecimalMax(value = "180.0")
    private Double lng;

    @NotNull
    @Future(message = "Shift start time must be in the future")
    private Instant startTime;

    @NotNull
    @Future(message = "Shift end time must be in the future")
    private Instant endTime;
}
