package com.group18.employeepersonnelmanagement.service.impl;

import com.group18.employeepersonnelmanagement.entity.OvertimeRequest;
import com.group18.employeepersonnelmanagement.mapper.OvertimeRequestMapper;
import com.group18.employeepersonnelmanagement.service.OvertimeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OvertimeServiceImpl implements OvertimeService {
    private final OvertimeRequestMapper overtimeRequestMapper;

    @Override
    public void submit(OvertimeRequest request) {
        double hours = Duration.between(request.getStartTime(), request.getEndTime()).toMinutes() / 60.0;
        request.setHours(Math.round(hours * 10.0) / 10.0);
        request.setStatus("PENDING");
        overtimeRequestMapper.insert(request);
    }

    @Override
    public IPage<OvertimeRequest> pageByEmployee(Long employeeId, Integer current, Integer size) {
        return overtimeRequestMapper.selectPage(new Page<>(current, size),
            new LambdaQueryWrapper<OvertimeRequest>().eq(OvertimeRequest::getEmployeeId, employeeId).orderByDesc(OvertimeRequest::getCreateTime));
    }

    @Override
    public IPage<OvertimeRequest> pagePending(Integer current, Integer size) {
        return overtimeRequestMapper.selectPage(new Page<>(current, size),
            new LambdaQueryWrapper<OvertimeRequest>().eq(OvertimeRequest::getStatus, "PENDING").orderByDesc(OvertimeRequest::getCreateTime));
    }

    @Override
    public void approve(Long id, String status, Long approverId, String comment) {
        OvertimeRequest req = new OvertimeRequest();
        req.setId(id);
        req.setStatus(status);
        req.setApproverId(approverId);
        req.setApproveComment(comment);
        req.setApproveTime(LocalDateTime.now());
        overtimeRequestMapper.updateById(req);
    }
}