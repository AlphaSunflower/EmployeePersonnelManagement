package com.group18.employeepersonnelmanagement.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.group18.employeepersonnelmanagement.entity.*;
import com.group18.employeepersonnelmanagement.mapper.*;
import com.group18.employeepersonnelmanagement.service.EmployeeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeMapper employeeMapper;
    private final SysUserMapper sysUserMapper;
    private final DepartmentMapper departmentMapper;
    private final PositionMapper positionMapper;
    private final PasswordEncoder passwordEncoder;

    @Value("${aliyun.oss.endpoint}") private String endpoint;
    @Value("${aliyun.oss.access-key-id}") private String accessKeyId;
    @Value("${aliyun.oss.access-key-secret}") private String accessKeySecret;
    @Value("${aliyun.oss.bucket-name}") private String bucketName;

    @Override
    public IPage<Employee> page(Integer current, Integer size, String keyword, Long deptId, String status) {
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(Employee::getName, keyword)
                    .or().like(Employee::getPhone, keyword)
                    .or().like(Employee::getEmail, keyword));
        }
        if (deptId != null) wrapper.eq(Employee::getDeptId, deptId);
        if (status != null && !status.isEmpty()) wrapper.eq(Employee::getStatus, status);
        wrapper.orderByDesc(Employee::getCreateTime);
        return employeeMapper.selectPage(new Page<>(current, size), wrapper);
    }

    @Override
    public Employee getById(Long id) {
        return employeeMapper.selectById(id);
    }

    @Override
    public Employee getCurrentEmployee(Long userId) {
        SysUser user = sysUserMapper.selectById(userId);
        if (user == null || user.getEmployeeId() == null) throw new RuntimeException("Employee not bound");
        return employeeMapper.selectById(user.getEmployeeId());
    }

    @Override
    public void save(Employee employee) {
        employeeMapper.insert(employee);
        // Create sys_user for the employee
        SysUser user = new SysUser();
        user.setUsername(employee.getPhone() != null ? employee.getPhone() : "emp_" + employee.getId());
        user.setPassword(passwordEncoder.encode("123456")); // default: 123456
        user.setRole("EMPLOYEE");
        user.setEmployeeId(employee.getId());
        user.setStatus(1);
        sysUserMapper.insert(user);
    }

    @Override
    public void update(Employee employee) {
        employeeMapper.updateById(employee);
    }

    @Override
    public void delete(Long id) {
        Employee emp = new Employee();
        emp.setId(id);
        emp.setStatus("RESIGNED");
        employeeMapper.updateById(emp);
    }

    @Override
    public void changeStatus(Long id, String newStatus, Long newDeptId, Long newPositionId) {
        Employee emp = new Employee();
        emp.setId(id);
        emp.setStatus(newStatus);
        if (newDeptId != null) emp.setDeptId(newDeptId);
        if (newPositionId != null) emp.setPositionId(newPositionId);
        employeeMapper.updateById(emp);
    }

    @Override
    public String uploadAvatar(Long employeeId, MultipartFile file) {
        String originalName = file.getOriginalFilename();
        String ext = originalName != null && originalName.contains(".") 
            ? originalName.substring(originalName.lastIndexOf(".")) : ".jpg";
        String objectName = "avatars/" + employeeId + "_" + System.currentTimeMillis() + ext;
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            ossClient.putObject(bucketName, objectName, file.getInputStream());
            String url = "https://" + bucketName + "." + endpoint + "/" + objectName;
            Employee emp = new Employee();
            emp.setId(employeeId);
            emp.setAvatarUrl(url);
            employeeMapper.updateById(emp);
            return url;
        } catch (Exception e) {
            throw new RuntimeException("Avatar upload failed: " + e.getMessage());
        } finally {
            ossClient.shutdown();
        }
    }

    @Override
    public void updateProfile(Long employeeId, Map<String, String> updates) {
        Employee emp = new Employee();
        emp.setId(employeeId);
        if (updates.containsKey("phone")) emp.setPhone(updates.get("phone"));
        if (updates.containsKey("email")) emp.setEmail(updates.get("email"));
        if (updates.containsKey("emergencyContact")) emp.setEmergencyContact(updates.get("emergencyContact"));
        if (updates.containsKey("emergencyPhone")) emp.setEmergencyPhone(updates.get("emergencyPhone"));
        employeeMapper.updateById(emp);
    }

    @Override
    public void exportRoster(HttpServletResponse response) {
        List<Employee> employees = employeeMapper.selectList(null);
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Employee Roster");
            Row header = sheet.createRow(0);
            String[] cols = {"Name", "Gender", "ID Card", "Phone", "Email", "Hire Date", "Department", "Position", "Status"};
            for (int i = 0; i < cols.length; i++) header.createCell(i).setCellValue(cols[i]);
            int rowIdx = 1;
            for (Employee e : employees) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(e.getName());
                row.createCell(1).setCellValue(e.getGender() == 1 ? "Male" : "Female");
                row.createCell(2).setCellValue(e.getIdCard());
                row.createCell(3).setCellValue(e.getPhone());
                row.createCell(4).setCellValue(e.getEmail());
                row.createCell(5).setCellValue(e.getHireDate() != null ? e.getHireDate().toString() : "");
                Department dept = departmentMapper.selectById(e.getDeptId());
                row.createCell(6).setCellValue(dept != null ? dept.getName() : "");
                Position pos = positionMapper.selectById(e.getPositionId());
                row.createCell(7).setCellValue(pos != null ? pos.getName() : "");
                row.createCell(8).setCellValue(e.getStatus());
            }
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("Employee_Roster.xlsx", StandardCharsets.UTF_8));
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException("Export failed: " + e.getMessage());
        }
    }
}
