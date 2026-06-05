package com.group18.employeepersonnelmanagement.controller;

import com.group18.employeepersonnelmanagement.common.Result;
import com.group18.employeepersonnelmanagement.entity.OvertimeRequest;
import com.group18.employeepersonnelmanagement.service.OvertimeService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/overtime-requests")
@RequiredArgsConstructor
public class OvertimeController {
    private final OvertimeService overtimeService;

    @PostMapping
    public Result<?> submit(@RequestHeader("X-Employee-Id") Long employeeId, @RequestBody OvertimeRequest req) {
        req.setEmployeeId(employeeId);
        overtimeService.submit(req);
        return Result.success();
    }

    @GetMapping
    public Result<IPage<OvertimeRequest>> myRecords(@RequestHeader("X-Employee-Id") Long employeeId,
                                                      @RequestParam(defaultValue = "1") Integer current,
                                                      @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(overtimeService.pageByEmployee(employeeId, current, size));
    }

    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<IPage<OvertimeRequest>> pending(@RequestParam(defaultValue = "1") Integer current,
                                                    @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(overtimeService.pagePending(current, size));
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<?> approve(@PathVariable Long id, @RequestBody Map<String, String> body) {
        overtimeService.approve(id, body.get("status"), Long.valueOf(body.get("approverId")), body.get("comment"));
        return Result.success();
    }
}
