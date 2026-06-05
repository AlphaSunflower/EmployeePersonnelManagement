package com.group18.employeepersonnelmanagement.service.impl;

import com.group18.employeepersonnelmanagement.entity.MakeupRequest;
import com.group18.employeepersonnelmanagement.mapper.MakeupRequestMapper;
import com.group18.employeepersonnelmanagement.service.MakeupService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MakeupServiceImpl implements MakeupService {
    private final MakeupRequestMapper makeupRequestMapper;

    @Override
    public void submit(MakeupRequest request) {
        request.setStatus("PENDING");
        makeupRequestMapper.insert(request);
    }

    @Override
    public IPage<MakeupRequest> pageByEmployee(Long employeeId, Integer current, Integer size) {
        return makeupRequestMapper.selectPage(new Page<>(current, size),
            new LambdaQueryWrapper<MakeupRequest>().eq(MakeupRequest::getEmployeeId, employeeId).orderByDesc(MakeupRequest::getCreateTime));
    }

    @Override
    public IPage<MakeupRequest> pagePending(Integer current, Integer size) {
        return makeupRequestMapper.selectPage(new Page<>(current, size),
            new LambdaQueryWrapper<MakeupRequest>().eq(MakeupRequest::getStatus, "PENDING").orderByDesc(MakeupRequest::getCreateTime));
    }

    @Override
    public void approve(Long id, String status, Long approverId, String comment) {
        MakeupRequest req = new MakeupRequest();
        req.setId(id);
        req.setStatus(status);
        req.setApproverId(approverId);
        req.setApproveComment(comment);
        req.setApproveTime(LocalDateTime.now());
        makeupRequestMapper.updateById(req);
    }
}
