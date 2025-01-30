package com.banking.authservice.dto;

import lombok.Data;

@Data
public class LoginDto {
    private String username;
    private String password;
}