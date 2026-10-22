package com.gigsup.dto;

import com.gigsup.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 8, max = 72)
    private String password;

    @NotNull
    private Role role;

    @NotBlank
    @Pattern(
        regexp = "^\\+?[1-9]\\d{9,14}$",
        message = "Enter a valid phone number with country code"
    )
    private String phoneNumber;
}