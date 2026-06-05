package com.group18.employeepersonnelmanagement.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("overtime_request")
public class OvertimeRequest extends BaseEntity {
    private Long employeeId;
    private LocalDate date;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double hours;
    private String reason;
    private String status;
    private Long approverId;
    private LocalDateTime approveTime;
    private String approveComment;
}
