package com.gigsup.repository;

import com.gigsup.entity.OtpVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpVerificationRepository
        extends JpaRepository<OtpVerification, Long> {

    Optional<OtpVerification> findByPhoneNumber(String phoneNumber);

    Optional<OtpVerification> findByUserId(Long userId);

    void deleteByUserId(Long userId);
}