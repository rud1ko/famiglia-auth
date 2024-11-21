package com.famiglia.famiglia_auth.dto;

import com.famiglia.famiglia_auth.enums.Role;
import lombok.Data;

@Data
public class SignUpRequest {
    private String username;
    private String email;
    private String password;
    private Role role;
}