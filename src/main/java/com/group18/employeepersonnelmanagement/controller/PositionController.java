package com.group18.employeepersonnelmanagement.controller;

import com.group18.employeepersonnelmanagement.common.Result;
import com.group18.employeepersonnelmanagement.entity.Position;
import com.group18.employeepersonnelmanagement.service.PositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/positions")
@RequiredArgsConstructor
public class PositionController {
    private final PositionService positionService;

    @GetMapping
    public Result<List<Position>> listAll() { return Result.success(positionService.listAll()); }

    @GetMapping("/{id}")
    public Result<Position> getById(@PathVariable Long id) { return Result.success(positionService.getById(id)); }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Result<?> save(@RequestBody Position pos) { positionService.save(pos); return Result.success(); }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<?> update(@PathVariable Long id, @RequestBody Position pos) { pos.setId(id); positionService.update(pos); return Result.success(); }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<?> delete(@PathVariable Long id) { positionService.delete(id); return Result.success(); }
}
