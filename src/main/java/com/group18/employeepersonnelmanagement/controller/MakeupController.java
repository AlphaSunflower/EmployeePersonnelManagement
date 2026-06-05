package com.group18.employeepersonnelmanagement.controller;

import com.group18.employeepersonnelmanagement.common.Result;
import com.group18.employeepersonnelmanagement.entity.MakeupRequest;
import com.group18.employeepersonnelmanagement.service.MakeupService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/makeup-requests")
@RequiredArgsConstructor
public class MakeupController {
    private final MakeupService makeupService;

    @PostMapping
    public Result<?> submit(@RequestHeader("X-Employee-Id") Long employeeId, @RequestBody MakeupRequest req) {
        req.setEmployeeId(employeeId);
        makeupService.submit(req);
        return Result.success();
    }

    @GetMapping
    public Result<IPage<MakeupRequest>> myRecords(@RequestHeader("X-Employee-Id") Long employeeId,
                                                    @RequestParam(defaultValue = "1") Integer current,
                                                    @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(makeupService.pageByEmployee(employeeId, current, size));
    }

    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<IPage<MakeupRequest>> pending(@RequestParam(defaultValue = "1") Integer current,
                                                  @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(makeupService.pagePending(current, size));
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<?> approve(@PathVariable Long id, @RequestBody Map<String, String> body) {
        makeupService.approve(id, body.get("status"), Long.valueOf(body.get("approverId")), body.get("comment"));
        return Result.success();
    }
}
