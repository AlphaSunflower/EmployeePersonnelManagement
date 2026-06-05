package com.group18.employeepersonnelmanagement.service;

import com.group18.employeepersonnelmanagement.entity.Department;
import java.util.List;
import java.util.Map;

public interface DepartmentService {
    List<Department> listAll();
    List<Department> getTree();
    Department getById(Long id);
    void save(Department department);
    void update(Department department);
    void delete(Long id);
    List<Map<String, Object>> getOrganizationTree();
}
