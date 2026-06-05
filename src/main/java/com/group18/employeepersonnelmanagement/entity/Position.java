package com.group18.employeepersonnelmanagement.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("position")
public class Position extends BaseEntity {
    private String name;
    private Long deptId;
    private String description;
    private Integer status;
    private Integer deleted;
}
