package com.example.demo.dto;

import java.math.BigDecimal;
import java.util.Date;

public record OrderDTO(
        ItemDTO item,
        CustomerDTO customer,
        Date orderDate,
        BigDecimal shipping,
        BigDecimal total,
        BigDecimal taxRate,
        AddressDTO billingAddress,
        AddressDTO shippingAddress
) {
}
