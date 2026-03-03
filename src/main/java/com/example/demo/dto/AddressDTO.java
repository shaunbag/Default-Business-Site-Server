package com.example.demo.dto;

public record AddressDTO(
        String street,
        String city,
        String county,
        String postCode,
        String country
) {
}
