package com.group18.employeepersonnelmanagement.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("employee")
public class Employee extends BaseEntity {
    private String name;
    private Integer gender;
    private String idCard;
    private String phone;
    private String email;
    private LocalDate hireDate;
    private Long deptId;
    private Long positionId;
    private String level;
    private String status;
    private String avatarUrl;
    private String emergencyContact;
    private String emergencyPhone;
    private Integer deleted;
}
