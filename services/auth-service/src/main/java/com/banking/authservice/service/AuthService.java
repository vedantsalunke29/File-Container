package com.banking.authservice.service;

import com.banking.authservice.dto.AuthResponseDto;
import com.banking.authservice.dto.LoginDto;
import com.banking.authservice.dto.RegisterDto;

public interface AuthService {
    AuthResponseDto login(LoginDto loginDto);
    void register(RegisterDto registerDto);
}
