package com.gigsup.entity;

import com.gigsup.enums.ShiftStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "shifts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Shift {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vendor_id", nullable = false)
    private User vendor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private User student; // null until MATCHED

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(nullable = false)
    @DecimalMin(value = "0.01", message = "Pay rate must be greater than zero")
    private BigDecimal payRate;

    @Column(columnDefinition = "geometry(Point,4326)", nullable = false)
    private Point location;

    @Column(nullable = false)
    private Instant startTime;

    @Column(nullable = false)
    private Instant endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ShiftStatus status = ShiftStatus.OPEN;

    // Prevents race conditions: two students accepting the same shift simultaneously
    @Version
    private Long version;

    @Builder.Default
    private Instant createdAt = Instant.now();
}
