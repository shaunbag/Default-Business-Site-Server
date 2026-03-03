package com.example.demo.controller;

import com.example.demo.dto.AdminUserDTO;
import com.example.demo.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin-users")
public class AdminUserController {

    @Autowired
    private AdminUserService adminUserService;

    @GetMapping
    public ResponseEntity<List<AdminUserDTO>> getAllAdminUsers() {
        List<AdminUserDTO> adminUsers = adminUserService.getAll();
        return ResponseEntity.ok(adminUsers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminUserDTO> getAdminUserById(@PathVariable Long id) {
        Optional<AdminUserDTO> adminUser = adminUserService.getById(id);
        return adminUser.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AdminUserDTO> createAdminUser(@RequestBody AdminUserDTO adminUserDTO) {
        AdminUserDTO created = adminUserService.create(adminUserDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdminUserDTO> updateAdminUser(@PathVariable Long id, @RequestBody AdminUserDTO adminUserDTO) {
        Optional<AdminUserDTO> updated = adminUserService.update(id, adminUserDTO);
        return updated.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdminUser(@PathVariable Long id) {
        if (adminUserService.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
