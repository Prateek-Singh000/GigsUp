package com.gigsup.controller;

import com.gigsup.dto.ShiftMatchRequest;
import com.gigsup.dto.ShiftRequest;
import com.gigsup.dto.ShiftResponse;
import com.gigsup.entity.Shift;
import com.gigsup.service.ShiftService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shifts")
@RequiredArgsConstructor
public class ShiftController {

    private final ShiftService shiftService;

    @PostMapping
    public ShiftResponse postShift(@Valid @RequestBody ShiftRequest req) {
        return ShiftResponse.from(shiftService.postShift(req));
    }

    @PostMapping("/match")
    public List<ShiftResponse> match(@Valid @RequestBody ShiftMatchRequest req) {
        return shiftService.findMatches(req).stream().map(ShiftResponse::from).toList();
    }

    @PostMapping("/{id}/accept")
    public ShiftResponse accept(@PathVariable Long id) {
        return ShiftResponse.from(shiftService.acceptShift(id));
    }

    @PostMapping("/{id}/complete")
    public ShiftResponse complete(@PathVariable Long id) {
        return ShiftResponse.from(shiftService.completeShift(id));
    }

    @GetMapping("/posted")
    public List<ShiftResponse> posted() {
        return shiftService.myPostedShifts().stream().map(ShiftResponse::from).toList();
    }

    @GetMapping("/accepted")
    public List<ShiftResponse> accepted() {
        return shiftService.myAcceptedShifts().stream().map(ShiftResponse::from).toList();
    }
}
