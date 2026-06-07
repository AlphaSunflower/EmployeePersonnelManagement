package com.group18.employeepersonnelmanagement.service;

import com.group18.employeepersonnelmanagement.dto.LoginRequest;
import com.group18.employeepersonnelmanagement.dto.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
    void changePassword(Long userId, String oldPassword, String newPassword);
}
