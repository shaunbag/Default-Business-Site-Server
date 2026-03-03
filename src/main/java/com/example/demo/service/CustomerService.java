package com.example.demo.service;

import com.example.demo.dto.CustomerDTO;
import com.example.demo.model.Customer;
import com.example.demo.model.Address;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AddressRepository addressRepository;

    public List<CustomerDTO> getAll() {
        return customerRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<CustomerDTO> getById(Long id) {
        return customerRepository.findById(id)
                .map(this::convertToDTO);
    }

    public CustomerDTO create(CustomerDTO dto) {
        Customer customer = new Customer(
                dto.username(),
                dto.email(),
                dto.password(),
                dto.createdAt()
        );
        Customer saved = customerRepository.save(customer);
        
        if (dto.addresses() != null && !dto.addresses().isEmpty()) {
            List<Address> addresses = dto.addresses().stream()
                    .map(addrDto -> new Address(
                            addrDto.street(),
                            addrDto.city(),
                            addrDto.county(),
                            addrDto.postCode(),
                            addrDto.country(),
                            saved
                    ))
                    .collect(Collectors.toList());
            addressRepository.saveAll(addresses);
            saved.setAddresses(new java.util.ArrayList<>(addresses));
        }
        
        return convertToDTO(saved);
    }

    public Optional<CustomerDTO> update(Long id, CustomerDTO dto) {
        Optional<Customer> existing = customerRepository.findById(id);
        if (existing.isPresent()) {
            Customer customer = existing.get();
            customer.setUsername(dto.username());
            customer.setEmail(dto.email());
            customer.setPassword(dto.password());
            customer.setCreatedAt(dto.createdAt());
            
            if (dto.addresses() != null) {
                // Delete existing addresses for this customer
                if (!customer.getAddresses().isEmpty()) {
                    addressRepository.deleteAll(customer.getAddresses());
                }
                
                // Create and save new addresses
                List<Address> addresses = dto.addresses().stream()
                        .map(addrDto -> new Address(
                                addrDto.street(),
                                addrDto.city(),
                                addrDto.county(),
                                addrDto.postCode(),
                                addrDto.country(),
                                customer
                        ))
                        .collect(Collectors.toList());
                List<Address> savedAddresses = addressRepository.saveAll(addresses);
                customer.setAddresses(new java.util.ArrayList<>(savedAddresses));
            }
            
            Customer updated = customerRepository.save(customer);
            return Optional.of(convertToDTO(updated));
        }
        return Optional.empty();
    }

    public boolean delete(Long id) {
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private CustomerDTO convertToDTO(Customer customer) {
        return new CustomerDTO(
                customer.getUsername(),
                customer.getEmail(),
                customer.getPassword(),
                customer.getCreatedAt(),
                customer.getAddresses().stream()
                        .map(addr -> new com.example.demo.dto.AddressDTO(
                                addr.getStreet(),
                                addr.getCity(),
                                addr.getCounty(),
                                addr.getPostCode(),
                                addr.getCountry()
                        ))
                        .collect(Collectors.toList())
        );
    }
}
