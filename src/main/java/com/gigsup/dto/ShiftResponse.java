package com.gigsup.dto;

import com.gigsup.entity.Shift;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class ShiftResponse {
    private Long id;
    private String title;
    private String description;
    private BigDecimal payRate;
    private double lat;
    private double lng;
    private Instant startTime;
    private Instant endTime;
    private String status;
    private Long vendorId;
    private String vendorName;
    private Long studentId;

    public static ShiftResponse from(Shift s) {
        ShiftResponse r = new ShiftResponse();
        r.setId(s.getId());
        r.setTitle(s.getTitle());
        r.setDescription(s.getDescription());
        r.setPayRate(s.getPayRate());
        r.setLat(s.getLocation().getY());
        r.setLng(s.getLocation().getX());
        r.setStartTime(s.getStartTime());
        r.setEndTime(s.getEndTime());
        r.setStatus(s.getStatus().name());
        r.setVendorId(s.getVendor().getId());
        r.setVendorName(s.getVendor().getName());
        r.setStudentId(s.getStudent() != null ? s.getStudent().getId() : null);
        return r;
    }
}
