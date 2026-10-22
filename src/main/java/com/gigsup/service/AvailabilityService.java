
package com.gigsup.service;

import com.gigsup.dto.AvailabilityRequest;
import com.gigsup.entity.Availability;
import com.gigsup.entity.User;
import com.gigsup.enums.Role;
import com.gigsup.exception.ApiException;
import com.gigsup.repository.AvailabilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AvailabilityService {

    private final AvailabilityRepository availabilityRepository;
    private final CurrentUserService currentUserService;

    public Availability addAvailability(AvailabilityRequest req) {
        User student = currentUserService.getCurrentUser();
        ensureStudent(student);

        if (!req.getAvailableFrom().isBefore(req.getAvailableTo())) {
            throw new ApiException(
                    "Availability start time must be before end time",
                    HttpStatus.BAD_REQUEST
            );
        }

        Availability availability = Availability.builder()
                .student(student)
                .availableFrom(req.getAvailableFrom())
                .availableTo(req.getAvailableTo())
                .build();

        return availabilityRepository.save(availability);
    }

    public List<Availability> myAvailability() {
        User student = currentUserService.getCurrentUser();
        ensureStudent(student);

        return availabilityRepository.findByStudentId(student.getId());
    }

    private void ensureStudent(User user) {
        if (user.getRole() != Role.STUDENT) {
            throw new ApiException(
                    "Only students can manage availability",
                    HttpStatus.FORBIDDEN
            );
        }
    }
}
