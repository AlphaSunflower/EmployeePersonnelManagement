package com.group18.employeepersonnelmanagement.service.impl;

import com.group18.employeepersonnelmanagement.entity.LeaveRequest;
import com.group18.employeepersonnelmanagement.entity.OvertimeRequest;
import com.group18.employeepersonnelmanagement.entity.MakeupRequest;
import com.group18.employeepersonnelmanagement.mapper.*;
import com.group18.employeepersonnelmanagement.service.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class LeaveServiceImpl implements LeaveService {
    private final LeaveRequestMapper leaveRequestMapper;

    @Override
    public void submit(LeaveRequest request) {
        long days = ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate()) + 1;
        request.setDays((double) days);
        request.setStatus("PENDING");
        leaveRequestMapper.insert(request);
    }

    @Override
    public IPage<LeaveRequest> pageByEmployee(Long employeeId, Integer current, Integer size) {
        return leaveRequestMapper.selectPage(new Page<>(current, size),
            new LambdaQueryWrapper<LeaveRequest>().eq(LeaveRequest::getEmployeeId, employeeId).orderByDesc(LeaveRequest::getCreateTime));
    }

    @Override
    public IPage<LeaveRequest> pagePending(Integer current, Integer size) {
        return leaveRequestMapper.selectPage(new Page<>(current, size),
            new LambdaQueryWrapper<LeaveRequest>().eq(LeaveRequest::getStatus, "PENDING").orderByDesc(LeaveRequest::getCreateTime));
    }

    @Override
    public void approve(Long id, String status, Long approverId, String comment) {
        LeaveRequest req = new LeaveRequest();
        req.setId(id);
        req.setStatus(status);
        req.setApproverId(approverId);
        req.setApproveComment(comment);
        req.setApproveTime(LocalDateTime.now());
        leaveRequestMapper.updateById(req);
    }
}
