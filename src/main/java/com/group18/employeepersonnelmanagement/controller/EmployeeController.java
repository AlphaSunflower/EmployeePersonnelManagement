package com.group18.employeepersonnelmanagement.controller;

import com.group18.employeepersonnelmanagement.common.Result;
import com.group18.employeepersonnelmanagement.entity.Employee;
import com.group18.employeepersonnelmanagement.service.EmployeeService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Result<IPage<Employee>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long deptId,
            @RequestParam(required = false) String status) {
        return Result.success(employeeService.page(current, size, keyword, deptId, status));
    }

    @GetMapping("/{id}")
    public Result<Employee> getById(@PathVariable Long id) { return Result.success(employeeService.getById(id)); }

    @GetMapping("/me")
    public Result<Employee> me(@RequestHeader("X-User-Id") Long userId) { return Result.success(employeeService.getCurrentEmployee(userId)); }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Result<?> save(@RequestBody Employee emp) { employeeService.save(emp); return Result.success(); }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<?> update(@PathVariable Long id, @RequestBody Employee emp) { emp.setId(id); employeeService.update(emp); return Result.success(); }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<?> delete(@PathVariable Long id) { employeeService.delete(id); return Result.success(); }

    @PostMapping("/{id}/change")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<?> changeStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        employeeService.changeStatus(id,
            body.get("status"),
            body.get("deptId") != null ? Long.valueOf(body.get("deptId")) : null,
            body.get("positionId") != null ? Long.valueOf(body.get("positionId")) : null);
        return Result.success();
    }

    @PutMapping("/me")
    public Result<?> updateProfile(@RequestHeader("X-User-Id") Long userId, @RequestBody Map<String, String> updates) {
        employeeService.updateProfile(employeeService.getCurrentEmployee(userId).getId(), updates);
        return Result.success();
    }

    @PostMapping("/me/avatar")
    public Result<String> uploadAvatar(@RequestHeader("X-User-Id") Long userId, @RequestParam("file") MultipartFile file) {
        return Result.success(employeeService.uploadAvatar(employeeService.getCurrentEmployee(userId).getId(), file));
    }

    @GetMapping("/export")
    @PreAuthorize("hasRole('ADMIN')")
    public void export(HttpServletResponse response) { employeeService.exportRoster(response); }
}
