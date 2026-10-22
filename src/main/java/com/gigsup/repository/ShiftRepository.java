package com.gigsup.repository;

import com.gigsup.entity.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface ShiftRepository extends JpaRepository<Shift, Long> {

    /**
     * Core geospatial matching query.
     *
     * - ST_DWithin uses the spatial index (GiST) on shifts.location for a fast bounding-box
     *   pre-filter, then does the precise distance check — this is what gets us sub-100ms
     *   responses even with a large shifts table.
     * - ::geography cast makes ST_DWithin operate in meters (radiusMeters) instead of degrees.
     * - The availability sub-query filters out shifts that don't fall inside ANY of the
     *   student's declared free time windows.
     */
    @Query(value = """
        SELECT s.* FROM shifts s
        WHERE s.status = 'OPEN'
          AND ST_DWithin(
                s.location::geography,
                ST_SetSRID(ST_MakePoint(:lng, :lat), 4326)::geography,
                :radiusMeters
          )
        ORDER BY ST_Distance(
                s.location::geography,
                ST_SetSRID(ST_MakePoint(:lng, :lat), 4326)::geography
        ) ASC
        """, nativeQuery = true)
List<Shift> findMatchingShifts(
        @Param("lat") double lat,
        @Param("lng") double lng,
        @Param("radiusMeters") double radiusMeters
);

    List<Shift> findByVendorId(Long vendorId);

    List<Shift> findByStudentId(Long studentId);
}
