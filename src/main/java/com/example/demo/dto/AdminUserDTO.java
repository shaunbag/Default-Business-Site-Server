package com.example.demo.dto;

import java.util.Date;

public record AdminUserDTO(
        String username,
        String email,
        String role,
        Date createdAt
) {
}
