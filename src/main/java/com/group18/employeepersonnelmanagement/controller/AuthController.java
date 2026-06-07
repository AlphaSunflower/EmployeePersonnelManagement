package com.group18.employeepersonnelmanagement.controller;

import com.group18.employeepersonnelmanagement.common.Result;
import com.group18.employeepersonnelmanagement.dto.LoginRequest;
import com.group18.employeepersonnelmanagement.dto.LoginResponse;
import com.group18.employeepersonnelmanagement.security.JwtTokenProvider;
import com.group18.employeepersonnelmanagement.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody LoginRequest request) {
        return Result.success(authService.login(request));
    }

    @PutMapping("/change-password")
    public Result<?> changePassword(@RequestHeader("Authorization") String authHeader, @RequestBody Map<String, String> body) {
        String token = authHeader.substring(7);
        Long userId = jwtTokenProvider.getUserId(token);
        authService.changePassword(userId, body.get("oldPassword"), body.get("newPassword"));
        return Result.success();
    }
}
