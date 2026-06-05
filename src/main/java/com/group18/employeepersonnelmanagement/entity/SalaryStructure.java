package com.group18.employeepersonnelmanagement.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("salary_structure")
public class SalaryStructure extends BaseEntity {
    private String name;
    private BigDecimal basicSalary;
    private BigDecimal performanceSalary;
    private BigDecimal subsidy;
    private String description;
    private Integer deleted;
}
