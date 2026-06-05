package com.group18.employeepersonnelmanagement.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("employee_salary")
public class EmployeeSalary extends BaseEntity {
    private Long employeeId;
    private Long structureId;
    private BigDecimal basicSalary;
    private BigDecimal performanceSalary;
    private BigDecimal subsidy;
}
