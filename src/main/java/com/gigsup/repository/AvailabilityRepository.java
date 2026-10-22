package com.gigsup.repository;

import com.gigsup.entity.Availability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
    List<Availability> findByStudentId(Long studentId);
}
