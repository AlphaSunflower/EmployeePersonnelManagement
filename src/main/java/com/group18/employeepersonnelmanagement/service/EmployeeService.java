package com.group18.employeepersonnelmanagement.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.group18.employeepersonnelmanagement.entity.Employee;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

public interface EmployeeService {
    IPage<Employee> page(Integer current, Integer size, String keyword, Long deptId, String status);
    Employee getById(Long id);
    Employee getCurrentEmployee(Long userId);
    void save(Employee employee);
    void update(Employee employee);
    void delete(Long id);
    void changeStatus(Long id, String newStatus, Long newDeptId, Long newPositionId);
    String uploadAvatar(Long employeeId, MultipartFile file);
    void updateProfile(Long employeeId, Map<String, String> updates);
    void exportRoster(HttpServletResponse response);
}
