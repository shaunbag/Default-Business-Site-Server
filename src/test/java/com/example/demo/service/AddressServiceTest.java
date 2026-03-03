package com.example.demo.service;

import com.example.demo.dto.AddressDTO;
import com.example.demo.model.Address;
import com.example.demo.repository.AddressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private AddressService addressService;

    private Address testAddress;
    private AddressDTO testAddressDTO;

    @BeforeEach
    void setUp() {
        testAddress = new Address(
                "123 Main St",
                "New York",
                "NY",
                "10001",
                "USA",
                null
        );
        testAddress.setId(1L);

        testAddressDTO = new AddressDTO(
                "123 Main St",
                "New York",
                "NY",
                "10001",
                "USA"
        );
    }

    @Test
    void testGetAll() {
        when(addressRepository.findAll()).thenReturn(Arrays.asList(testAddress));

        var result = addressService.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("123 Main St", result.get(0).street());
        verify(addressRepository, times(1)).findAll();
    }

    @Test
    void testGetByIdSuccess() {
        when(addressRepository.findById(1L)).thenReturn(Optional.of(testAddress));

        var result = addressService.getById(1L);

        assertTrue(result.isPresent());
        assertEquals("123 Main St", result.get().street());
        verify(addressRepository, times(1)).findById(1L);
    }

    @Test
    void testGetByIdNotFound() {
        when(addressRepository.findById(99L)).thenReturn(Optional.empty());

        var result = addressService.getById(99L);

        assertFalse(result.isPresent());
        verify(addressRepository, times(1)).findById(99L);
    }

    @Test
    void testCreate() {
        when(addressRepository.save(any(Address.class))).thenReturn(testAddress);

        var result = addressService.create(testAddressDTO);

        assertNotNull(result);
        assertEquals("123 Main St", result.street());
        assertEquals("New York", result.city());
        verify(addressRepository, times(1)).save(any(Address.class));
    }

    @Test
    void testUpdateSuccess() {
        when(addressRepository.findById(1L)).thenReturn(Optional.of(testAddress));
        when(addressRepository.save(any(Address.class))).thenReturn(testAddress);

        var result = addressService.update(1L, testAddressDTO);

        assertTrue(result.isPresent());
        assertEquals("123 Main St", result.get().street());
        verify(addressRepository, times(1)).findById(1L);
        verify(addressRepository, times(1)).save(any(Address.class));
    }

    @Test
    void testUpdateNotFound() {
        when(addressRepository.findById(99L)).thenReturn(Optional.empty());

        var result = addressService.update(99L, testAddressDTO);

        assertFalse(result.isPresent());
        verify(addressRepository, times(1)).findById(99L);
        verify(addressRepository, never()).save(any(Address.class));
    }

    @Test
    void testDeleteSuccess() {
        when(addressRepository.existsById(1L)).thenReturn(true);

        var result = addressService.delete(1L);

        assertTrue(result);
        verify(addressRepository, times(1)).existsById(1L);
        verify(addressRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteNotFound() {
        when(addressRepository.existsById(99L)).thenReturn(false);

        var result = addressService.delete(99L);

        assertFalse(result);
        verify(addressRepository, times(1)).existsById(99L);
        verify(addressRepository, never()).deleteById(any());
    }
}
