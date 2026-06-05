package com.group18.employeepersonnelmanagement.controller;

import com.group18.employeepersonnelmanagement.common.Result;
import com.group18.employeepersonnelmanagement.entity.*;
import com.group18.employeepersonnelmanagement.service.SalaryService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/salary")
@RequiredArgsConstructor
public class SalaryController {
    private final SalaryService salaryService;

    @GetMapping("/structures")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<List<SalaryStructure>> structures() { return Result.success(salaryService.listStructures()); }

    @PostMapping("/structures")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<?> saveStructure(@RequestBody SalaryStructure s) { salaryService.saveStructure(s); return Result.success(); }

    @PutMapping("/structures/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<?> updateStructure(@PathVariable Long id, @RequestBody SalaryStructure s) { s.setId(id); salaryService.updateStructure(s); return Result.success(); }

    @DeleteMapping("/structures/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<?> deleteStructure(@PathVariable Long id) { salaryService.deleteStructure(id); return Result.success(); }

    @GetMapping("/employee-salaries")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<IPage<Map<String, Object>>> empSalaries(@RequestParam(defaultValue = "1") Integer current,
                                                           @RequestParam(defaultValue = "10") Integer size,
                                                           @RequestParam(required = false) String keyword) {
        return Result.success(salaryService.pageEmployeeSalaries(current, size, keyword));
    }

    @PostMapping("/employee-salaries")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<?> saveEmpSalary(@RequestBody EmployeeSalary s) { salaryService.saveEmployeeSalary(s); return Result.success(); }

    @PutMapping("/employee-salaries/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<?> updateEmpSalary(@PathVariable Long id, @RequestBody EmployeeSalary s) { s.setId(id); salaryService.updateEmployeeSalary(s); return Result.success(); }

    @PostMapping("/calculate")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<?> calculate(@RequestParam Integer year, @RequestParam Integer month) {
        salaryService.calculateMonthly(year, month);
        return Result.success();
    }

    @GetMapping("/records")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<IPage<SalaryRecord>> records(@RequestParam(defaultValue = "1") Integer current,
                                                @RequestParam(defaultValue = "10") Integer size,
                                                @RequestParam(required = false) Integer year,
                                                @RequestParam(required = false) Integer month) {
        return Result.success(salaryService.pageRecords(current, size, year, month));
    }

    @GetMapping("/records/export")
    @PreAuthorize("hasRole('ADMIN')")
    public void export(HttpServletResponse response, @RequestParam Integer year, @RequestParam Integer month) {
        salaryService.exportRecords(response, year, month);
    }

    @GetMapping("/records/{id}")
    public Result<SalaryRecord> payslip(@PathVariable Long id) { return Result.success(salaryService.getPayslip(id)); }

    @GetMapping("/records/me")
    public Result<List<SalaryRecord>> myRecords(@RequestHeader("X-Employee-Id") Long employeeId,
                                                  @RequestParam(required = false) Integer year,
                                                  @RequestParam(required = false) Integer month) {
        return Result.success(salaryService.getEmployeeRecords(employeeId, year, month));
    }
}
