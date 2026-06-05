package com.group18.employeepersonnelmanagement.controller;

import com.group18.employeepersonnelmanagement.common.Result;
import com.group18.employeepersonnelmanagement.entity.LeaveRequest;
import com.group18.employeepersonnelmanagement.service.LeaveService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/leave-requests")
@RequiredArgsConstructor
public class LeaveController {
    private final LeaveService leaveService;

    @PostMapping
    public Result<?> submit(@RequestHeader("X-Employee-Id") Long employeeId, @RequestBody LeaveRequest req) {
        req.setEmployeeId(employeeId);
        leaveService.submit(req);
        return Result.success();
    }

    @GetMapping
    public Result<IPage<LeaveRequest>> myLeaves(@RequestHeader("X-Employee-Id") Long employeeId,
                                                 @RequestParam(defaultValue = "1") Integer current,
                                                 @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(leaveService.pageByEmployee(employeeId, current, size));
    }

    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<IPage<LeaveRequest>> pending(@RequestParam(defaultValue = "1") Integer current,
                                                @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(leaveService.pagePending(current, size));
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<?> approve(@PathVariable Long id, @RequestBody Map<String, String> body) {
        leaveService.approve(id, body.get("status"), Long.valueOf(body.get("approverId")), body.get("comment"));
        return Result.success();
    }
}
