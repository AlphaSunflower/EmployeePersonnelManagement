package com.group18.employeepersonnelmanagement.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("salary_record")
public class SalaryRecord extends BaseEntity {
    private Long employeeId;
    private Integer year;
    private Integer month;
    private BigDecimal basicSalary;
    private BigDecimal performanceSalary;
    private BigDecimal subsidy;
    private BigDecimal attendanceDeduction;
    private BigDecimal socialInsurancePersonal;
    private BigDecimal housingFundPersonal;
    private BigDecimal taxableIncome;
    private BigDecimal tax;
    private BigDecimal netSalary;
    private String status;
}
