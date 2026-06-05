package com.group18.employeepersonnelmanagement.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.group18.employeepersonnelmanagement.entity.LeaveRequest;
import java.util.List;

public interface LeaveService {
    void submit(LeaveRequest request);
    IPage<LeaveRequest> pageByEmployee(Long employeeId, Integer current, Integer size);
    IPage<LeaveRequest> pagePending(Integer current, Integer size);
    void approve(Long id, String status, Long approverId, String comment);
}
