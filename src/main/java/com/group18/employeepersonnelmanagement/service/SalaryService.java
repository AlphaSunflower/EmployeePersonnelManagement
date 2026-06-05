package com.group18.employeepersonnelmanagement.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.Map;
import com.group18.employeepersonnelmanagement.entity.*;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

public interface SalaryService {
    List<SalaryStructure> listStructures();
    void saveStructure(SalaryStructure structure);
    void updateStructure(SalaryStructure structure);
    void deleteStructure(Long id);
    IPage<Map<String, Object>> pageEmployeeSalaries(Integer current, Integer size, String keyword);
    void saveEmployeeSalary(EmployeeSalary salary);
    void updateEmployeeSalary(EmployeeSalary salary);
    void calculateMonthly(Integer year, Integer month);
    IPage<SalaryRecord> pageRecords(Integer current, Integer size, Integer year, Integer month);
    SalaryRecord getPayslip(Long id);
    List<SalaryRecord> getEmployeeRecords(Long employeeId, Integer year, Integer month);
    void exportRecords(HttpServletResponse response, Integer year, Integer month);
}
