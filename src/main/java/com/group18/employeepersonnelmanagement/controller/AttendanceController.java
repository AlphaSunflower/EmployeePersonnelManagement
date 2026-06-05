package com.group18.employeepersonnelmanagement.controller;

import com.group18.employeepersonnelmanagement.common.Result;
import com.group18.employeepersonnelmanagement.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {
    private final AttendanceService attendanceService;

    @PostMapping("/check-in")
    public Result<Map<String, Object>> checkIn(@RequestHeader("X-Employee-Id") Long employeeId) {
        return Result.success(attendanceService.checkIn(employeeId));
    }

    @PostMapping("/check-out")
    public Result<Map<String, Object>> checkOut(@RequestHeader("X-Employee-Id") Long employeeId) {
        return Result.success(attendanceService.checkOut(employeeId));
    }

    @GetMapping("/today")
    public Result<Map<String, Object>> today(@RequestHeader("X-Employee-Id") Long employeeId) {
        return Result.success(attendanceService.getTodayStatus(employeeId));
    }

    @GetMapping("/monthly")
    public Result<Map<String, Object>> monthly(@RequestHeader("X-Employee-Id") Long employeeId,
                                                @RequestParam Integer year, @RequestParam Integer month) {
        return Result.success(attendanceService.getMonthlySummary(employeeId, year, month));
    }

    @GetMapping("/monthly/all")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<List<Map<String, Object>>> allMonthly(@RequestParam Integer year, @RequestParam Integer month) {
        return Result.success(attendanceService.getAllMonthlySummary(year, month));
    }
}
