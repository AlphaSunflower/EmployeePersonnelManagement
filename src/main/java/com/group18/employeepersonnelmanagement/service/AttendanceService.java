package com.group18.employeepersonnelmanagement.service;

import java.util.List;
import java.util.Map;

public interface AttendanceService {
    Map<String, Object> checkIn(Long employeeId);
    Map<String, Object> checkOut(Long employeeId);
    Map<String, Object> getTodayStatus(Long employeeId);
    Map<String, Object> getMonthlySummary(Long employeeId, Integer year, Integer month);
    List<Map<String, Object>> getAllMonthlySummary(Integer year, Integer month);
}
