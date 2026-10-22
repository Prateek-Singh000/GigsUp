
package com.gigsup.service;

import com.gigsup.dto.AuthResponse;
import com.gigsup.dto.LoginRequest;
import com.gigsup.dto.RegisterRequest;
import com.gigsup.dto.RegistrationResponse;
import com.gigsup.entity.User;
import com.gigsup.exception.ApiException;
import com.gigsup.repository.UserRepository;
import com.gigsup.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final OtpService otpService;

    @Transactional
    public RegistrationResponse register(RegisterRequest req) {

        if (userRepository.existsByEmail(req.getEmail())) {
            throw new ApiException(
                    "Email already in use",
                    HttpStatus.CONFLICT
            );
        }

        if (userRepository.existsByPhoneNumber(req.getPhoneNumber())) {
            throw new ApiException(
                    "Phone number already in use",
                    HttpStatus.CONFLICT
            );
        }

        User user = User.builder()
                .name(req.getName())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .role(req.getRole())
                .phoneNumber(req.getPhoneNumber())
                .phoneVerified(false)
                .build();

        user = userRepository.save(user);
        otpService.generateOtp(user);

        return new RegistrationResponse(
                "Registration successful. Verify the OTP printed in the backend console.",
                user.getPhoneNumber()
        );
    }

    @Transactional(noRollbackFor = ApiException.class)
    public RegistrationResponse verifyPhone(
            String phoneNumber,
            String otp
    ) {
        otpService.verifyOtp(phoneNumber, otp);

        return new RegistrationResponse(
                "Phone number verified successfully. You can now log in.",
                phoneNumber
        );
    }

    @Transactional
    public RegistrationResponse resendOtp(String phoneNumber) {

        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new ApiException(
                        "User not found.",
                        HttpStatus.NOT_FOUND
                ));

        if (user.isPhoneVerified()) {
            throw new ApiException(
                    "Phone number is already verified.",
                    HttpStatus.BAD_REQUEST
            );
        }

        otpService.generateOtp(user);

        return new RegistrationResponse(
                "New OTP generated. Check the backend console.",
                phoneNumber
        );
    }

    public AuthResponse login(LoginRequest req) {

        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new ApiException(
                        "Invalid credentials",
                        HttpStatus.UNAUTHORIZED
                ));

        if (!passwordEncoder.matches(
                req.getPassword(),
                user.getPassword()
        )) {
            throw new ApiException(
                    "Invalid credentials",
                    HttpStatus.UNAUTHORIZED
            );
        }

        if (!user.isPhoneVerified()) {
            throw new ApiException(
                    "Please verify your phone number before logging in.",
                    HttpStatus.FORBIDDEN
            );
        }

        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRole().name(),
                user.getId()
        );

        return new AuthResponse(
                token,
                user.getEmail(),
                user.getRole().name(),
                user.getId()
        );
    }
}
