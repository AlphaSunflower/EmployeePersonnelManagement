package com.group18.employeepersonnelmanagement.controller;

import com.group18.employeepersonnelmanagement.common.Result;
import com.group18.employeepersonnelmanagement.entity.ReportingRelationship;
import com.group18.employeepersonnelmanagement.mapper.ReportingRelationshipMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reporting-relationships")
@RequiredArgsConstructor
public class ReportingController {
    private final ReportingRelationshipMapper mapper;

    @GetMapping
    public Result<?> get(@RequestParam Long employeeId) {
        return Result.success(mapper.selectOne(new LambdaQueryWrapper<ReportingRelationship>()
            .eq(ReportingRelationship::getEmployeeId, employeeId)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Result<?> save(@RequestBody ReportingRelationship rr) { mapper.insert(rr); return Result.success(); }
}
