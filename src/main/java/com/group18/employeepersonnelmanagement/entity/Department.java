package com.group18.employeepersonnelmanagement.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("department")
public class Department extends BaseEntity {
    private String name;
    private Long parentId;
    private Integer sortOrder;
    private Integer status;
    private Integer deleted;

    @TableField(exist = false)
    private List<Department> children;
}
