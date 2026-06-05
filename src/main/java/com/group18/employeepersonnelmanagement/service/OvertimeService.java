package com.group18.employeepersonnelmanagement.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.group18.employeepersonnelmanagement.entity.OvertimeRequest;

public interface OvertimeService {
    void submit(OvertimeRequest request);
    IPage<OvertimeRequest> pageByEmployee(Long employeeId, Integer current, Integer size);
    IPage<OvertimeRequest> pagePending(Integer current, Integer size);
    void approve(Long id, String status, Long approverId, String comment);
}
