package com.example.demo.dto;

import java.math.BigDecimal;

public record ItemDTO(
        String title,
        String description,
        String image,
        BigDecimal price,
        Long stock
) {
}
