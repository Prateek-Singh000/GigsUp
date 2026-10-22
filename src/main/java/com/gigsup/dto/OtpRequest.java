package com.gigsup.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class OtpRequest {

    @NotBlank
    @Pattern(
        regexp = "^\\+?[1-9]\\d{9,14}$",
        message = "Enter a valid phone number with country code"
    )
    private String phoneNumber;
}