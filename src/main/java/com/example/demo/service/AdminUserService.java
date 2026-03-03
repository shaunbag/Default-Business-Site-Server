package com.example.demo.service;

import com.example.demo.dto.AdminUserDTO;
import com.example.demo.model.AdminUser;
import com.example.demo.repository.AdminUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminUserService {

    @Autowired
    private AdminUserRepository adminUserRepository;

    public List<AdminUserDTO> getAll() {
        return adminUserRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<AdminUserDTO> getById(Long id) {
        return adminUserRepository.findById(id)
                .map(this::convertToDTO);
    }

    public AdminUserDTO create(AdminUserDTO dto) {
        AdminUser adminUser = new AdminUser();
        adminUser.setUsername(dto.username());
        adminUser.setEmail(dto.email());
        adminUser.setRole(dto.role());
        adminUser.setCreatedAt(dto.createdAt());
        AdminUser saved = adminUserRepository.save(adminUser);
        return convertToDTO(saved);
    }

    public Optional<AdminUserDTO> update(Long id, AdminUserDTO dto) {
        Optional<AdminUser> existing = adminUserRepository.findById(id);
        if (existing.isPresent()) {
            AdminUser adminUser = existing.get();
            adminUser.setUsername(dto.username());
            adminUser.setEmail(dto.email());
            adminUser.setRole(dto.role());
            adminUser.setCreatedAt(dto.createdAt());
            AdminUser updated = adminUserRepository.save(adminUser);
            return Optional.of(convertToDTO(updated));
        }
        return Optional.empty();
    }

    public boolean delete(Long id) {
        if (adminUserRepository.existsById(id)) {
            adminUserRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private AdminUserDTO convertToDTO(AdminUser adminUser) {
        return new AdminUserDTO(
                adminUser.getUsername(),
                adminUser.getEmail(),
                adminUser.getRole(),
                adminUser.getCreatedAt()
        );
    }
}
