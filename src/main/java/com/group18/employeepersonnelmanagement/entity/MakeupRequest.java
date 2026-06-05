package com.group18.employeepersonnelmanagement.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("makeup_request")
public class MakeupRequest extends BaseEntity {
    private Long employeeId;
    private LocalDate date;
    private String type;
    private String reason;
    private String status;
    private Long approverId;
    private LocalDateTime approveTime;
    private String approveComment;
}
