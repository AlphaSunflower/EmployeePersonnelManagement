package com.group18.employeepersonnelmanagement.config;

import com.group18.employeepersonnelmanagement.entity.SysUser;
import com.group18.employeepersonnelmanagement.mapper.SysUserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final SysUserMapper sysUserMapper;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initAdmin() {
        return args -> {
            SysUser admin = sysUserMapper.selectOne(
                new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, "admin")
            );
            if (admin == null) {
                SysUser newAdmin = new SysUser();
                newAdmin.setUsername("admin");
                newAdmin.setPassword(passwordEncoder.encode("123456"));
                newAdmin.setRole("ADMIN");
                newAdmin.setStatus(1);
                sysUserMapper.insert(newAdmin);
                System.out.println("=== Admin created: admin / 123456 ===");
            }
            // Also fix employee passwords if they have bad hashes
            long cnt = sysUserMapper.selectCount(
                new LambdaQueryWrapper<SysUser>()
                    .eq(SysUser::getRole, "EMPLOYEE")
                    .like(SysUser::getPassword, "%N9qo8uLO%")
            );
            if (cnt > 0) {
                System.out.println("=== Fixing " + cnt + " employee passwords ===");
                sysUserMapper.selectList(null).forEach(u -> {
                    if (u.getPassword().contains("N9qo8uLO")) {
                        u.setPassword(passwordEncoder.encode("123456"));
                        sysUserMapper.updateById(u);
                    }
                });
            }
        };
    }
}