package com.group18.employeepersonnelmanagement.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("leave_request")
public class LeaveRequest extends BaseEntity {
    private Long employeeId;
    private String type;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double days;
    private String reason;
    private String status;
    private Long approverId;
    private LocalDateTime approveTime;
    private String approveComment;
}
