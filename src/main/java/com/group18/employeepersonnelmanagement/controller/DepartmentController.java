package com.group18.employeepersonnelmanagement.controller;

import com.group18.employeepersonnelmanagement.common.Result;
import com.group18.employeepersonnelmanagement.entity.Department;
import com.group18.employeepersonnelmanagement.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;

    @GetMapping
    public Result<List<Department>> tree() { return Result.success(departmentService.getTree()); }

    @GetMapping("/list")
    public Result<List<Department>> list() { return Result.success(departmentService.listAll()); }

    @GetMapping("/{id}")
    public Result<Department> getById(@PathVariable Long id) { return Result.success(departmentService.getById(id)); }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Result<?> save(@RequestBody Department dept) { departmentService.save(dept); return Result.success(); }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<?> update(@PathVariable Long id, @RequestBody Department dept) { dept.setId(id); departmentService.update(dept); return Result.success(); }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<?> delete(@PathVariable Long id) { departmentService.delete(id); return Result.success(); }

    @GetMapping("/organization-tree")
    public Result<List<Map<String, Object>>> orgTree() { return Result.success(departmentService.getOrganizationTree()); }
}
