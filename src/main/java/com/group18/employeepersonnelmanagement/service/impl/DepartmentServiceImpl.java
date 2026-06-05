package com.group18.employeepersonnelmanagement.service.impl;

import com.group18.employeepersonnelmanagement.entity.Department;
import com.group18.employeepersonnelmanagement.entity.Employee;
import com.group18.employeepersonnelmanagement.entity.Position;
import com.group18.employeepersonnelmanagement.entity.ReportingRelationship;
import com.group18.employeepersonnelmanagement.mapper.*;
import com.group18.employeepersonnelmanagement.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentMapper departmentMapper;
    private final PositionMapper positionMapper;
    private final EmployeeMapper employeeMapper;
    private final ReportingRelationshipMapper reportingRelationshipMapper;

    @Override
    public List<Department> listAll() {
        return departmentMapper.selectList(null);
    }

    @Override
    public List<Department> getTree() {
        List<Department> all = departmentMapper.selectList(null);
        Map<Long, List<Department>> childrenMap = all.stream()
                .filter(d -> d.getParentId() != null && d.getParentId() > 0)
                .collect(Collectors.groupingBy(Department::getParentId));
        return all.stream()
                .filter(d -> d.getParentId() == null || d.getParentId() == 0)
                .peek(d -> d.setChildren(buildChildren(d.getId(), childrenMap)))
                .collect(Collectors.toList());
    }

    private List<Department> buildChildren(Long parentId, Map<Long, List<Department>> childrenMap) {
        List<Department> children = childrenMap.getOrDefault(parentId, Collections.emptyList());
        children.forEach(c -> c.setChildren(buildChildren(c.getId(), childrenMap)));
        return children;
    }

    @Override
    public Department getById(Long id) {
        return departmentMapper.selectById(id);
    }

    @Override
    public void save(Department department) {
        departmentMapper.insert(department);
    }

    @Override
    public void update(Department department) {
        departmentMapper.updateById(department);
    }

    @Override
    public void delete(Long id) {
        long empCount = employeeMapper.selectCount(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Employee>()
                .eq(Employee::getDeptId, id)
        );
        long childCount = departmentMapper.selectCount(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Department>()
                .eq(Department::getParentId, id)
        );
        if (empCount > 0 || childCount > 0) {
            throw new RuntimeException("Dept has employees or sub-departments, cannot delete");
        }
        departmentMapper.deleteById(id);
    }

    @Override
    public List<Map<String, Object>> getOrganizationTree() {
        List<Department> depts = departmentMapper.selectList(null);
        List<Position> positions = positionMapper.selectList(null);
        List<Employee> employees = employeeMapper.selectList(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Employee>()
                .eq(Employee::getStatus, "ACTIVE")
        );
        Map<Long, List<Position>> posMap = positions.stream()
                .collect(Collectors.groupingBy(Position::getDeptId));
        Map<Long, List<Employee>> empMap = employees.stream()
                .collect(Collectors.groupingBy(Employee::getDeptId));

        return depts.stream().map(d -> {
            Map<String, Object> node = new LinkedHashMap<>();
            node.put("name", d.getName());
            node.put("type", "department");
            node.put("id", d.getId());
            List<Map<String, Object>> children = new ArrayList<>();
            List<Position> deptPositions = posMap.getOrDefault(d.getId(), Collections.emptyList());
            for (Position p : deptPositions) {
                Map<String, Object> posNode = new LinkedHashMap<>();
                posNode.put("name", p.getName());
                posNode.put("type", "position");
                List<Map<String, Object>> empNodes = empMap.getOrDefault(d.getId(), Collections.emptyList())
                    .stream().filter(e -> e.getPositionId() != null && e.getPositionId().equals(p.getId()))
                    .map(e -> {
                        Map<String, Object> en = new LinkedHashMap<>();
                        en.put("name", e.getName());
                        en.put("type", "employee");
                        en.put("id", e.getId());
                        return en;
                    }).collect(Collectors.toList());
                if (!empNodes.isEmpty()) posNode.put("children", empNodes);
                children.add(posNode);
            }
            if (!children.isEmpty()) node.put("children", children);
            return node;
        }).collect(Collectors.toList());
    }
}
