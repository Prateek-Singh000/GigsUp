
package com.gigsup.entity;

import com.gigsup.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
    name = "users",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "phone_number")
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password; // BCrypt hashed

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // Mobile number used for development OTP verification
    @Column(name = "phone_number", length = 16)
    private String phoneNumber;

    @Builder.Default
    @Column(name = "phone_verified", nullable = false)
    private boolean phoneVerified = false;

    // Live coordinates, used for geospatial matching (SRID 4326 = WGS84 lat/lng)
    @Column(columnDefinition = "geometry(Point,4326)")
    private Point location;

    @Builder.Default
    @OneToMany(mappedBy = "vendor", cascade = CascadeType.ALL)
    private List<Shift> postedShifts = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<Availability> availabilities = new ArrayList<>();

    @Builder.Default
    private Instant createdAt = Instant.now();
}
