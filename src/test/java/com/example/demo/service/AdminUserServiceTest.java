package com.example.demo.service;

import com.example.demo.dto.AdminUserDTO;
import com.example.demo.model.AdminUser;
import com.example.demo.repository.AdminUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminUserServiceTest {

    @Mock
    private AdminUserRepository adminUserRepository;

    @InjectMocks
    private AdminUserService adminUserService;

    private AdminUser testAdminUser;
    private AdminUserDTO testAdminUserDTO;

    @BeforeEach
    void setUp() {
        testAdminUser = new AdminUser();
        testAdminUser.setId(1L);
        testAdminUser.setUsername("admin");
        testAdminUser.setEmail("admin@email.com");
        testAdminUser.setRole("ADMIN");
        testAdminUser.setCreatedAt(new Date());

        testAdminUserDTO = new AdminUserDTO(
                "admin",
                "admin@email.com",
                "ADMIN",
                new Date()
        );
    }

    @Test
    void testGetAll() {
        when(adminUserRepository.findAll()).thenReturn(Arrays.asList(testAdminUser));

        var result = adminUserService.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("admin", result.get(0).username());
        verify(adminUserRepository, times(1)).findAll();
    }

    @Test
    void testGetByIdSuccess() {
        when(adminUserRepository.findById(1L)).thenReturn(Optional.of(testAdminUser));

        var result = adminUserService.getById(1L);

        assertTrue(result.isPresent());
        assertEquals("admin", result.get().username());
        verify(adminUserRepository, times(1)).findById(1L);
    }

    @Test
    void testGetByIdNotFound() {
        when(adminUserRepository.findById(99L)).thenReturn(Optional.empty());

        var result = adminUserService.getById(99L);

        assertFalse(result.isPresent());
        verify(adminUserRepository, times(1)).findById(99L);
    }

    @Test
    void testCreate() {
        when(adminUserRepository.save(any(AdminUser.class))).thenReturn(testAdminUser);

        var result = adminUserService.create(testAdminUserDTO);

        assertNotNull(result);
        assertEquals("admin", result.username());
        assertEquals("admin@email.com", result.email());
        assertEquals("ADMIN", result.role());
        verify(adminUserRepository, times(1)).save(any(AdminUser.class));
    }

    @Test
    void testUpdateSuccess() {
        when(adminUserRepository.findById(1L)).thenReturn(Optional.of(testAdminUser));
        when(adminUserRepository.save(any(AdminUser.class))).thenReturn(testAdminUser);

        var result = adminUserService.update(1L, testAdminUserDTO);

        assertTrue(result.isPresent());
        assertEquals("admin", result.get().username());
        verify(adminUserRepository, times(1)).findById(1L);
        verify(adminUserRepository, times(1)).save(any(AdminUser.class));
    }

    @Test
    void testUpdateNotFound() {
        when(adminUserRepository.findById(99L)).thenReturn(Optional.empty());

        var result = adminUserService.update(99L, testAdminUserDTO);

        assertFalse(result.isPresent());
        verify(adminUserRepository, times(1)).findById(99L);
        verify(adminUserRepository, never()).save(any(AdminUser.class));
    }

    @Test
    void testDeleteSuccess() {
        when(adminUserRepository.existsById(1L)).thenReturn(true);

        var result = adminUserService.delete(1L);

        assertTrue(result);
        verify(adminUserRepository, times(1)).existsById(1L);
        verify(adminUserRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteNotFound() {
        when(adminUserRepository.existsById(99L)).thenReturn(false);

        var result = adminUserService.delete(99L);

        assertFalse(result);
        verify(adminUserRepository, times(1)).existsById(99L);
        verify(adminUserRepository, never()).deleteById(any());
    }
}
