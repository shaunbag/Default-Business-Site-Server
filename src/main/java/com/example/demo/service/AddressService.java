package com.example.demo.service;

import com.example.demo.dto.AddressDTO;
import com.example.demo.model.Address;
import com.example.demo.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    public List<AddressDTO> getAll() {
        return addressRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<AddressDTO> getById(Long id) {
        return addressRepository.findById(id)
                .map(this::convertToDTO);
    }

    public AddressDTO create(AddressDTO dto) {
        Address address = new Address(
                dto.street(),
                dto.city(),
                dto.county(),
                dto.postCode(),
                dto.country(),
                null
        );
        Address saved = addressRepository.save(address);
        return convertToDTO(saved);
    }

    public Optional<AddressDTO> update(Long id, AddressDTO dto) {
        Optional<Address> existing = addressRepository.findById(id);
        if (existing.isPresent()) {
            Address address = existing.get();
            address.setStreet(dto.street());
            address.setCity(dto.city());
            address.setCounty(dto.county());
            address.setPostCode(dto.postCode());
            address.setCountry(dto.country());
            Address updated = addressRepository.save(address);
            return Optional.of(convertToDTO(updated));
        }
        return Optional.empty();
    }

    public boolean delete(Long id) {
        if (addressRepository.existsById(id)) {
            addressRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private AddressDTO convertToDTO(Address address) {
        return new AddressDTO(
                address.getStreet(),
                address.getCity(),
                address.getCounty(),
                address.getPostCode(),
                address.getCountry()
        );
    }
}
