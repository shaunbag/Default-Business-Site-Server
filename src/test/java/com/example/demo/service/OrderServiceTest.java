package com.example.demo.service;

import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.ItemDTO;
import com.example.demo.dto.CustomerDTO;
import com.example.demo.dto.AddressDTO;
import com.example.demo.model.CustomerOrder;
import com.example.demo.model.Item;
import com.example.demo.model.Customer;
import com.example.demo.model.Address;
import com.example.demo.repository.AddressRepository;
import com.example.demo.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private OrderService orderService;

    private CustomerOrder testOrder;
    private OrderDTO testOrderDTO;
    private Item testItem;
    private Customer testCustomer;
    private Address testBillingAddress;
    private Address testShippingAddress;

    @BeforeEach
    void setUp() {
        testItem = new Item("Test Item", "Description", "image.jpg", new BigDecimal("29.99"), 100L);
        testItem.setId(1L);

        testCustomer = new Customer("testuser", "test@email.com", "password", new Date());
        testCustomer.setId(1L);
        testCustomer.setAddresses(new ArrayList<>());

        testBillingAddress = new Address("123 Main St", "New York", "NY", "10001", "USA", null);
        testBillingAddress.setId(1L);

        testShippingAddress = new Address("456 Oak Ave", "Los Angeles", "CA", "90001", "USA", null);
        testShippingAddress.setId(2L);

        testOrder = new CustomerOrder(testItem, testCustomer, new Date(), new BigDecimal("5.00"), new BigDecimal("34.99"));
        testOrder.setId(1L);
        testOrder.setTaxRate(new BigDecimal("0.08"));
        testOrder.setBillingAddress(testBillingAddress);
        testOrder.setShippingAddress(testShippingAddress);

        testOrderDTO = new OrderDTO(
                new ItemDTO("Test Item", "Description", "image.jpg", new BigDecimal("29.99"), 100L),
                new CustomerDTO("testuser", "test@email.com", "password", new Date(), new ArrayList<>()),
                new Date(),
                new BigDecimal("5.00"),
                new BigDecimal("34.99"),
                new BigDecimal("0.08"),
                new AddressDTO("123 Main St", "New York", "NY", "10001", "USA"),
                new AddressDTO("456 Oak Ave", "Los Angeles", "CA", "90001", "USA")
        );
    }

    @Test
    void testGetAll() {
        when(orderRepository.findAll()).thenReturn(Arrays.asList(testOrder));

        var result = orderService.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void testGetByIdSuccess() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));

        var result = orderService.getById(1L);

        assertTrue(result.isPresent());
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void testGetByIdNotFound() {
        when(orderRepository.findById(99L)).thenReturn(Optional.empty());

        var result = orderService.getById(99L);

        assertFalse(result.isPresent());
        verify(orderRepository, times(1)).findById(99L);
    }

    @Test
    void testCreateWithAddresses() {
        when(addressRepository.save(any(Address.class)))
                .thenReturn(testBillingAddress)
                .thenReturn(testShippingAddress);
        when(orderRepository.save(any(CustomerOrder.class))).thenReturn(testOrder);

        var result = orderService.create(testOrderDTO);

        assertNotNull(result);
        verify(addressRepository, times(2)).save(any(Address.class));
        verify(orderRepository, times(1)).save(any(CustomerOrder.class));
    }

    @Test
    void testUpdateSuccess() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(addressRepository.save(any(Address.class)))
                .thenReturn(testBillingAddress)
                .thenReturn(testShippingAddress);
        when(orderRepository.save(any(CustomerOrder.class))).thenReturn(testOrder);

        var result = orderService.update(1L, testOrderDTO);

        assertTrue(result.isPresent());
        verify(orderRepository, times(1)).findById(1L);
        verify(addressRepository, times(2)).delete(any(Address.class));
        verify(addressRepository, times(2)).save(any(Address.class));
        verify(orderRepository, times(1)).save(any(CustomerOrder.class));
    }

    @Test
    void testUpdateNotFound() {
        when(orderRepository.findById(99L)).thenReturn(Optional.empty());

        var result = orderService.update(99L, testOrderDTO);

        assertFalse(result.isPresent());
        verify(orderRepository, times(1)).findById(99L);
        verify(orderRepository, never()).save(any(CustomerOrder.class));
    }

    @Test
    void testDeleteSuccess() {
        when(orderRepository.existsById(1L)).thenReturn(true);

        var result = orderService.delete(1L);

        assertTrue(result);
        verify(orderRepository, times(1)).existsById(1L);
        verify(orderRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteNotFound() {
        when(orderRepository.existsById(99L)).thenReturn(false);

        var result = orderService.delete(99L);

        assertFalse(result);
        verify(orderRepository, times(1)).existsById(99L);
        verify(orderRepository, never()).deleteById(any());
    }
}
