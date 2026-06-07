package com.group18.employeepersonnelmanagement.service.impl;

import com.group18.employeepersonnelmanagement.dto.LoginRequest;
import com.group18.employeepersonnelmanagement.dto.LoginResponse;
import com.group18.employeepersonnelmanagement.entity.SysUser;
import com.group18.employeepersonnelmanagement.mapper.SysUserMapper;
import com.group18.employeepersonnelmanagement.security.JwtTokenProvider;
import com.group18.employeepersonnelmanagement.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final SysUserMapper sysUserMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public LoginResponse login(LoginRequest request) {
        SysUser user = sysUserMapper.selectOne(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, request.getUsername())
        );
        if (user == null || user.getStatus() == 0) {
            throw new RuntimeException("Account does not exist or is disabled");
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Incorrect password");
        }
        String token = jwtTokenProvider.generateToken(user.getId(), user.getUsername(), user.getRole());
        return new LoginResponse(token, user.getRole(), user.getId(), user.getEmployeeId(), user.getUsername());
    }

    @Override
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        if (newPassword == null || newPassword.length() < 6) {
            throw new RuntimeException("New password must be at least 6 characters");
        }
        SysUser user = sysUserMapper.selectById(userId);
        if (user == null || user.getStatus() == 0) {
            throw new RuntimeException("Invalid credentials");
        }
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        sysUserMapper.updateById(user);
    }
}
