package com.group18.employeepersonnelmanagement.service.impl;

import com.group18.employeepersonnelmanagement.common.GlobalExceptionHandler;
import com.group18.employeepersonnelmanagement.entity.*;
import com.group18.employeepersonnelmanagement.mapper.*;
import com.group18.employeepersonnelmanagement.service.SalaryService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SalaryServiceImpl implements SalaryService {

    private final SalaryStructureMapper structureMapper;
    private final EmployeeSalaryMapper employeeSalaryMapper;
    private final SalaryRecordMapper salaryRecordMapper;
    private final EmployeeMapper employeeMapper;
    private final AttendanceMapper attendanceMapper;
    private final LeaveRequestMapper leaveRequestMapper;

    private static final BigDecimal SOCIAL_INSURANCE_RATE = new BigDecimal("0.105");
    private static final BigDecimal HOUSING_FUND_RATE = new BigDecimal("0.07");
    private static final BigDecimal TAX_THRESHOLD = new BigDecimal("5000");
    private static final BigDecimal LATE_PENALTY = new BigDecimal("50");
    private static final BigDecimal ABSENT_PENALTY = new BigDecimal("200");

    @Override
    public List<SalaryStructure> listStructures() {
        return structureMapper.selectList(null);
    }

    @Override
    public void saveStructure(SalaryStructure s) {
        structureMapper.insert(s);
    }

    @Override
    public void updateStructure(SalaryStructure s) {
        structureMapper.updateById(s);
    }

    @Override
    public void deleteStructure(Long id) {
        structureMapper.deleteById(id);
    }

    @Override
    public IPage<Map<String, Object>> pageEmployeeSalaries(Integer current, Integer size, String keyword) {
        Page<EmployeeSalary> page = new Page<>(current, size);
        LambdaQueryWrapper<EmployeeSalary> wrapper = new LambdaQueryWrapper<>();
        IPage<EmployeeSalary> result = employeeSalaryMapper.selectPage(page, wrapper);
        IPage<Map<String, Object>> mapPage = new Page<>(current, size, result.getTotal());
        List<Map<String, Object>> list = new ArrayList<>();
        for (EmployeeSalary es : result.getRecords()) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", es.getId());
            m.put("employeeId", es.getEmployeeId());
            Employee emp = employeeMapper.selectById(es.getEmployeeId());
            m.put("employeeName", emp != null ? emp.getName() : "");
            m.put("basicSalary", es.getBasicSalary());
            m.put("performanceSalary", es.getPerformanceSalary());
            m.put("subsidy", es.getSubsidy());
            list.add(m);
        }
        mapPage.setRecords(list);
        return mapPage;
    }

    @Override
    public void saveEmployeeSalary(EmployeeSalary salary) {
        //查询他的salary是否已有
        LambdaQueryWrapper<EmployeeSalary> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EmployeeSalary::getEmployeeId, salary.getEmployeeId());
        EmployeeSalary employeeSalary = employeeSalaryMapper.selectOne(wrapper);

        if (employeeSalary == null){
            employeeSalaryMapper.insert(salary);
        }else {
            throw new RuntimeException("员工已存在");
        }


    }

    @Override
    public void updateEmployeeSalary(EmployeeSalary salary) {
        employeeSalaryMapper.updateById(salary);
    }

    @Override
    public void calculateMonthly(Integer year, Integer month) {
        List<EmployeeSalary> salaries = employeeSalaryMapper.selectList(null);
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.plusMonths(1);

        for (EmployeeSalary es : salaries) {
            long lateCount = attendanceMapper.selectCount(new LambdaQueryWrapper<Attendance>()
                    .eq(Attendance::getEmployeeId, es.getEmployeeId())
                    .ge(Attendance::getDate, start).lt(Attendance::getDate, end)
                    .eq(Attendance::getStatus, "LATE"));
            long absentCount = attendanceMapper.selectCount(new LambdaQueryWrapper<Attendance>()
                    .eq(Attendance::getEmployeeId, es.getEmployeeId())
                    .ge(Attendance::getDate, start).lt(Attendance::getDate, end)
                    .eq(Attendance::getStatus, "ABSENT"));
            long personalLeaveDays = leaveRequestMapper.selectList(new LambdaQueryWrapper<LeaveRequest>()
                            .eq(LeaveRequest::getEmployeeId, es.getEmployeeId())
                            .eq(LeaveRequest::getType, "PERSONAL").eq(LeaveRequest::getStatus, "APPROVED")
                            .ge(LeaveRequest::getStartDate, start).lt(LeaveRequest::getStartDate, end))
                    .stream().mapToLong(l -> l.getDays().longValue()).sum();

            BigDecimal gross = es.getBasicSalary().add(es.getPerformanceSalary()).add(es.getSubsidy());
            BigDecimal dailyRate = gross.divide(new BigDecimal("21.75"), 2, RoundingMode.HALF_UP);
            BigDecimal deduction = LATE_PENALTY.multiply(BigDecimal.valueOf(lateCount))
                    .add(ABSENT_PENALTY.multiply(BigDecimal.valueOf(absentCount)))
                    .add(dailyRate.multiply(BigDecimal.valueOf(personalLeaveDays)));

            BigDecimal socialIns = gross.multiply(SOCIAL_INSURANCE_RATE).setScale(2, RoundingMode.HALF_UP);
            BigDecimal housingFund = gross.multiply(HOUSING_FUND_RATE).setScale(2, RoundingMode.HALF_UP);
            BigDecimal taxable = gross.subtract(deduction).subtract(socialIns).subtract(housingFund).subtract(TAX_THRESHOLD);
            BigDecimal tax = BigDecimal.ZERO;
            if (taxable.compareTo(BigDecimal.ZERO) > 0) {
                if (taxable.compareTo(new BigDecimal("3000")) <= 0) {
                    tax = taxable.multiply(new BigDecimal("0.03"));
                } else if (taxable.compareTo(new BigDecimal("12000")) <= 0) {
                    tax = taxable.multiply(new BigDecimal("0.10")).subtract(new BigDecimal("210"));
                } else if (taxable.compareTo(new BigDecimal("25000")) <= 0) {
                    tax = taxable.multiply(new BigDecimal("0.20")).subtract(new BigDecimal("1410"));
                } else if (taxable.compareTo(new BigDecimal("35000")) <= 0) {
                    tax = taxable.multiply(new BigDecimal("0.25")).subtract(new BigDecimal("2660"));
                } else if (taxable.compareTo(new BigDecimal("55000")) <= 0) {
                    tax = taxable.multiply(new BigDecimal("0.30")).subtract(new BigDecimal("4410"));
                } else if (taxable.compareTo(new BigDecimal("80000")) <= 0) {
                    tax = taxable.multiply(new BigDecimal("0.35")).subtract(new BigDecimal("7160"));
                } else {
                    tax = taxable.multiply(new BigDecimal("0.45")).subtract(new BigDecimal("15160"));
                }
            }
            tax = tax.max(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);
            BigDecimal net = gross.subtract(deduction).subtract(socialIns).subtract(housingFund).subtract(tax);

            SalaryRecord record = salaryRecordMapper.selectOne(new LambdaQueryWrapper<SalaryRecord>()
                    .eq(SalaryRecord::getEmployeeId, es.getEmployeeId())
                    .eq(SalaryRecord::getYear, year).eq(SalaryRecord::getMonth, month));
            if (record == null) {
                record = new SalaryRecord();
            }
            record.setEmployeeId(es.getEmployeeId());
            record.setYear(year);
            record.setMonth(month);
            record.setBasicSalary(es.getBasicSalary());
            record.setPerformanceSalary(es.getPerformanceSalary());
            record.setSubsidy(es.getSubsidy());
            record.setAttendanceDeduction(deduction);
            record.setSocialInsurancePersonal(socialIns);
            record.setHousingFundPersonal(housingFund);
            record.setTaxableIncome(taxable.max(BigDecimal.ZERO));
            record.setTax(tax);
            record.setNetSalary(net);
            record.setStatus("CONFIRMED");
            if (record.getId() == null) {
                salaryRecordMapper.insert(record);
            } else {
                salaryRecordMapper.updateById(record);
            }
        }
    }

    @Override
    public IPage<SalaryRecord> pageRecords(Integer current, Integer size, Integer year, Integer month) {
        return salaryRecordMapper.selectPage(new Page<>(current, size),
                new LambdaQueryWrapper<SalaryRecord>()
                        .eq(year != null, SalaryRecord::getYear, year)
                        .eq(month != null, SalaryRecord::getMonth, month)
                        .orderByDesc(SalaryRecord::getYear).orderByDesc(SalaryRecord::getMonth));
    }

    @Override
    public SalaryRecord getPayslip(Long id) {
        return salaryRecordMapper.selectById(id);
    }

    @Override
    public List<SalaryRecord> getEmployeeRecords(Long employeeId, Integer year, Integer month) {
        return salaryRecordMapper.selectList(new LambdaQueryWrapper<SalaryRecord>()
                .eq(SalaryRecord::getEmployeeId, employeeId)
                .eq(year != null, SalaryRecord::getYear, year)
                .eq(month != null, SalaryRecord::getMonth, month)
                .orderByDesc(SalaryRecord::getYear).orderByDesc(SalaryRecord::getMonth));
    }

    @Override
    public void exportRecords(HttpServletResponse response, Integer year, Integer month) {
        List<SalaryRecord> records = salaryRecordMapper.selectList(new LambdaQueryWrapper<SalaryRecord>()
                .eq(SalaryRecord::getYear, year).eq(SalaryRecord::getMonth, month));
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Monthly Salary");
            Row header = sheet.createRow(0);
            String[] cols = {"Employee ID", "Basic", "Performance", "Subsidy", "Deduction", "Social Ins", "Housing Fund", "Taxable", "Tax", "Net Salary"};
            for (int i = 0; i < cols.length; i++) {
                header.createCell(i).setCellValue(cols[i]);
            }
            int rowIdx = 1;
            for (SalaryRecord r : records) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(r.getEmployeeId());
                row.createCell(1).setCellValue(r.getBasicSalary().doubleValue());
                row.createCell(2).setCellValue(r.getPerformanceSalary().doubleValue());
                row.createCell(3).setCellValue(r.getSubsidy().doubleValue());
                row.createCell(4).setCellValue(r.getAttendanceDeduction().doubleValue());
                row.createCell(5).setCellValue(r.getSocialInsurancePersonal().doubleValue());
                row.createCell(6).setCellValue(r.getHousingFundPersonal().doubleValue());
                row.createCell(7).setCellValue(r.getTaxableIncome().doubleValue());
                row.createCell(8).setCellValue(r.getTax().doubleValue());
                row.createCell(9).setCellValue(r.getNetSalary().doubleValue());
            }
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("Salary_" + year + "_" + month + ".xlsx", StandardCharsets.UTF_8));
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException("Export failed: " + e.getMessage());
        }
    }
}
