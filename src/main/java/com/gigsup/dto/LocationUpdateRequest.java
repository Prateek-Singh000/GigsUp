package com.gigsup.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LocationUpdateRequest {
    @NotNull
    private Double lat;
    @NotNull
    private Double lng;
}
