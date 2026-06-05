package com.group18.employeepersonnelmanagement.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("reporting_relationship")
public class ReportingRelationship extends BaseEntity {
    private Long employeeId;
    private Long supervisorId;
}
