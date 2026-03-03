package com.example.demo.dto;

import java.util.Date;
import java.util.List;

public record CustomerDTO(
        String username,
        String email,
        String password,
        Date createdAt,
        List<AddressDTO> addresses
) {
}
