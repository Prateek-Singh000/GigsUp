
package com.gigsup.controller;

import com.gigsup.dto.AuthResponse;
import com.gigsup.dto.LoginRequest;
import com.gigsup.dto.OtpVerifyRequest;
import com.gigsup.dto.RegisterRequest;
import com.gigsup.dto.RegistrationResponse;
import com.gigsup.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public RegistrationResponse register(
            @Valid @RequestBody RegisterRequest req
    ) {
        return authService.register(req);
    }

    @PostMapping("/verify-phone")
    public RegistrationResponse verifyPhone(
            @Valid @RequestBody OtpVerifyRequest req
    ) {
        return authService.verifyPhone(
                req.getPhoneNumber(),
                req.getOtp()
        );
    }

    @PostMapping("/resend-otp")
    public RegistrationResponse resendOtp(
            @RequestParam String phoneNumber
    ) {
        return authService.resendOtp(phoneNumber);
    }

    @PostMapping("/login")
    public AuthResponse login(
            @Valid @RequestBody LoginRequest req
    ) {
        return authService.login(req);
    }
}
