package com.group18.employeepersonnelmanagement.service.impl;

import com.group18.employeepersonnelmanagement.entity.Attendance;
import com.group18.employeepersonnelmanagement.mapper.AttendanceMapper;
import com.group18.employeepersonnelmanagement.mapper.EmployeeMapper;
import com.group18.employeepersonnelmanagement.service.AttendanceService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceMapper attendanceMapper;
    private final EmployeeMapper employeeMapper;

    private static final LocalTime CHECK_IN_DEADLINE = LocalTime.of(9, 0);
    private static final LocalTime CHECK_OUT_EARLIEST = LocalTime.of(18, 0);
    private static final String WIFI_NAME = "CompanyWiFi-5G";

    @Override
    public Map<String, Object> checkIn(Long employeeId) {
        LocalDate today = LocalDate.now();
        Attendance existing = attendanceMapper.selectOne(
            new LambdaQueryWrapper<Attendance>()
                .eq(Attendance::getEmployeeId, employeeId)
                .eq(Attendance::getDate, today)
        );
        if (existing != null && existing.getCheckIn() != null) {
            throw new RuntimeException("Already checked in today");
        }
        String status = LocalTime.now().isAfter(CHECK_IN_DEADLINE) ? "LATE" : "NORMAL";
        if (existing == null) {
            existing = new Attendance();
            existing.setEmployeeId(employeeId);
            existing.setDate(today);
            existing.setCheckIn(LocalDateTime.now());
            existing.setWifiName(WIFI_NAME);
            existing.setStatus(status);
            attendanceMapper.insert(existing);
        } else {
            existing.setCheckIn(LocalDateTime.now());
            existing.setStatus(status);
            attendanceMapper.updateById(existing);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("checkIn", existing.getCheckIn());
        result.put("status", status);
        result.put("wifiName", WIFI_NAME);
        return result;
    }

    @Override
    public Map<String, Object> checkOut(Long employeeId) {
        LocalDate today = LocalDate.now();
        Attendance existing = attendanceMapper.selectOne(
            new LambdaQueryWrapper<Attendance>()
                .eq(Attendance::getEmployeeId, employeeId)
                .eq(Attendance::getDate, today)
        );
        if (existing == null || existing.getCheckIn() == null) {
            throw new RuntimeException("Please check in first");
        }
        if (existing.getCheckOut() != null) {
            throw new RuntimeException("Already checked out today");
        }
        boolean isEarly = LocalTime.now().isBefore(CHECK_OUT_EARLIEST);
        if (isEarly && "LATE".equals(existing.getStatus())) {
            existing.setStatus("LATE");
        } else if (isEarly) {
            existing.setStatus("EARLY");
        } else {
            existing.setStatus(existing.getStatus());
        }
        existing.setCheckOut(LocalDateTime.now());
        attendanceMapper.updateById(existing);
        Map<String, Object> result = new HashMap<>();
        result.put("checkOut", existing.getCheckOut());
        result.put("status", existing.getStatus());
        return result;
    }

    @Override
    public Map<String, Object> getTodayStatus(Long employeeId) {
        Attendance att = attendanceMapper.selectOne(
            new LambdaQueryWrapper<Attendance>()
                .eq(Attendance::getEmployeeId, employeeId)
                .eq(Attendance::getDate, LocalDate.now())
        );
        Map<String, Object> result = new HashMap<>();
        result.put("checkedIn", att != null && att.getCheckIn() != null);
        result.put("checkedOut", att != null && att.getCheckOut() != null);
        if (att != null) {
            result.put("checkIn", att.getCheckIn());
            result.put("checkOut", att.getCheckOut());
            result.put("status", att.getStatus());
        }
        return result;
    }

    @Override
    public Map<String, Object> getMonthlySummary(Long employeeId, Integer year, Integer month) {
        List<Attendance> records = attendanceMapper.selectList(
            new LambdaQueryWrapper<Attendance>()
                .eq(Attendance::getEmployeeId, employeeId)
                .ge(Attendance::getDate, LocalDate.of(year, month, 1))
                .lt(Attendance::getDate, LocalDate.of(year, month, 1).plusMonths(1))
        );
        long normal = 0, late = 0, early = 0, absent = 0;
        for (Attendance a : records) {
            switch (a.getStatus()) {
                case "NORMAL": normal++; break;
                case "LATE": late++; break;
                case "EARLY": early++; break;
                case "ABSENT": absent++; break;
            }
        }
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("total", records.size());
        result.put("normal", normal);
        result.put("late", late);
        result.put("early", early);
        result.put("absent", absent);
        result.put("records", records);
        return result;
    }

    @Override
    public List<Map<String, Object>> getAllMonthlySummary(Integer year, Integer month) {
        List<Attendance> records = attendanceMapper.selectList(
            new LambdaQueryWrapper<Attendance>()
                .ge(Attendance::getDate, LocalDate.of(year, month, 1))
                .lt(Attendance::getDate, LocalDate.of(year, month, 1).plusMonths(1))
        );
        Map<Long, Map<String, Object>> empMap = new LinkedHashMap<>();
        for (Attendance a : records) {
            empMap.putIfAbsent(a.getEmployeeId(), new LinkedHashMap<>());
            Map<String, Object> emp = empMap.get(a.getEmployeeId());
            emp.put("employeeId", a.getEmployeeId());
            emp.merge("total", 1, (old, n) -> (int)old + 1);
            String status = a.getStatus();
            emp.merge(status, 1, (old, n) -> (int)old + 1);
        }
        return new ArrayList<>(empMap.values());
    }
}
