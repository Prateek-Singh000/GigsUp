
package com.gigsup.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ShiftMatchRequest {

    @NotNull
    @DecimalMin(value = "-90.0")
    @DecimalMax(value = "90.0")
    private Double lat;

    @NotNull
    @DecimalMin(value = "-180.0")
    @DecimalMax(value = "180.0")
    private Double lng;

    @NotNull
    @DecimalMin(value = "1.0", message = "Radius must be greater than zero")
    @DecimalMax(value = "100000.0", message = "Radius cannot exceed 100 km")
    private Double radiusMeters;
}
