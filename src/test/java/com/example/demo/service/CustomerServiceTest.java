package com.example.demo.service;

import com.example.demo.dto.CustomerDTO;
import com.example.demo.model.Customer;
import com.example.demo.repository.AddressRepository;
import com.example.demo.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer testCustomer;
    private CustomerDTO testCustomerDTO;

    @BeforeEach
    void setUp() {
        testCustomer = new Customer("testuser", "test@email.com", "password123", new Date());
        testCustomer.setId(1L);
        testCustomer.setAddresses(new ArrayList<>());

        testCustomerDTO = new CustomerDTO(
                "testuser",
                "test@email.com",
                "password123",
                new Date(),
                new ArrayList<>()
        );
    }

    @Test
    void testGetAll() {
        when(customerRepository.findAll()).thenReturn(Arrays.asList(testCustomer));

        var result = customerService.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("testuser", result.get(0).username());
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void testGetByIdSuccess() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));

        var result = customerService.getById(1L);

        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().username());
        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    void testGetByIdNotFound() {
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        var result = customerService.getById(99L);

        assertFalse(result.isPresent());
        verify(customerRepository, times(1)).findById(99L);
    }

    @Test
    void testCreate() {
        when(customerRepository.save(any(Customer.class))).thenReturn(testCustomer);

        var result = customerService.create(testCustomerDTO);

        assertNotNull(result);
        assertEquals("testuser", result.username());
        assertEquals("test@email.com", result.email());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void testUpdateSuccess() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(testCustomer);

        var result = customerService.update(1L, testCustomerDTO);

        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().username());
        verify(customerRepository, times(1)).findById(1L);
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void testUpdateNotFound() {
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        var result = customerService.update(99L, testCustomerDTO);

        assertFalse(result.isPresent());
        verify(customerRepository, times(1)).findById(99L);
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void testDeleteSuccess() {
        when(customerRepository.existsById(1L)).thenReturn(true);

        var result = customerService.delete(1L);

        assertTrue(result);
        verify(customerRepository, times(1)).existsById(1L);
        verify(customerRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteNotFound() {
        when(customerRepository.existsById(99L)).thenReturn(false);

        var result = customerService.delete(99L);

        assertFalse(result);
        verify(customerRepository, times(1)).existsById(99L);
        verify(customerRepository, never()).deleteById(any());
    }
}
