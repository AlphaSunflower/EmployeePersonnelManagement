package com.group18.employeepersonnelmanagement.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.group18.employeepersonnelmanagement.entity.MakeupRequest;

public interface MakeupService {
    void submit(MakeupRequest request);
    IPage<MakeupRequest> pageByEmployee(Long employeeId, Integer current, Integer size);
    IPage<MakeupRequest> pagePending(Integer current, Integer size);
    void approve(Long id, String status, Long approverId, String comment);
}
