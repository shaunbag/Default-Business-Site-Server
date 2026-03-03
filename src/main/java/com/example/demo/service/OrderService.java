package com.example.demo.service;

import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.ItemDTO;
import com.example.demo.dto.CustomerDTO;
import com.example.demo.dto.AddressDTO;
import com.example.demo.model.CustomerOrder;
import com.example.demo.model.Item;
import com.example.demo.model.Customer;
import com.example.demo.model.Address;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AddressRepository addressRepository;

    public List<OrderDTO> getAll() {
        return orderRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<OrderDTO> getById(Long id) {
        return orderRepository.findById(id)
                .map(this::convertToDTO);
    }

    public OrderDTO create(OrderDTO dto) {
        CustomerOrder order = new CustomerOrder();
        order.setOrderDate(dto.orderDate());
        order.setShipping(dto.shipping());
        order.setTotal(dto.total());
        order.setTaxRate(dto.taxRate());
        
        if (dto.billingAddress() != null) {
            Address billingAddr = new Address(
                    dto.billingAddress().street(),
                    dto.billingAddress().city(),
                    dto.billingAddress().county(),
                    dto.billingAddress().postCode(),
                    dto.billingAddress().country(),
                    null
            );
            Address savedBillingAddr = addressRepository.save(billingAddr);
            order.setBillingAddress(savedBillingAddr);
        }
        
        if (dto.shippingAddress() != null) {
            Address shippingAddr = new Address(
                    dto.shippingAddress().street(),
                    dto.shippingAddress().city(),
                    dto.shippingAddress().county(),
                    dto.shippingAddress().postCode(),
                    dto.shippingAddress().country(),
                    null
            );
            Address savedShippingAddr = addressRepository.save(shippingAddr);
            order.setShippingAddress(savedShippingAddr);
        }
        
        CustomerOrder saved = orderRepository.save(order);
        return convertToDTO(saved);
    }

    public Optional<OrderDTO> update(Long id, OrderDTO dto) {
        Optional<CustomerOrder> existing = orderRepository.findById(id);
        if (existing.isPresent()) {
            CustomerOrder order = existing.get();
            order.setOrderDate(dto.orderDate());
            order.setShipping(dto.shipping());
            order.setTotal(dto.total());
            order.setTaxRate(dto.taxRate());
            
            // Handle billing address
            if (dto.billingAddress() != null) {
                // Delete old billing address if exists
                if (order.getBillingAddress() != null) {
                    addressRepository.delete(order.getBillingAddress());
                }
                
                Address billingAddr = new Address(
                        dto.billingAddress().street(),
                        dto.billingAddress().city(),
                        dto.billingAddress().county(),
                        dto.billingAddress().postCode(),
                        dto.billingAddress().country(),
                        null
                );
                Address savedBillingAddr = addressRepository.save(billingAddr);
                order.setBillingAddress(savedBillingAddr);
            }
            
            // Handle shipping address
            if (dto.shippingAddress() != null) {
                // Delete old shipping address if exists
                if (order.getShippingAddress() != null) {
                    addressRepository.delete(order.getShippingAddress());
                }
                
                Address shippingAddr = new Address(
                        dto.shippingAddress().street(),
                        dto.shippingAddress().city(),
                        dto.shippingAddress().county(),
                        dto.shippingAddress().postCode(),
                        dto.shippingAddress().country(),
                        null
                );
                Address savedShippingAddr = addressRepository.save(shippingAddr);
                order.setShippingAddress(savedShippingAddr);
            }
            
            CustomerOrder updated = orderRepository.save(order);
            return Optional.of(convertToDTO(updated));
        }
        return Optional.empty();
    }

    public boolean delete(Long id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private OrderDTO convertToDTO(CustomerOrder order) {
        ItemDTO itemDTO = order.getItem() != null ? convertItemToDTO(order.getItem()) : null;
        CustomerDTO customerDTO = order.getCustomer() != null ? convertCustomerToDTO(order.getCustomer()) : null;
        AddressDTO billingDTO = order.getBillingAddress() != null ? convertAddressToDTO(order.getBillingAddress()) : null;
        AddressDTO shippingDTO = order.getShippingAddress() != null ? convertAddressToDTO(order.getShippingAddress()) : null;
        
        return new OrderDTO(
                itemDTO,
                customerDTO,
                order.getOrderDate(),
                order.getShipping(),
                order.getTotal(),
                order.getTaxRate(),
                billingDTO,
                shippingDTO
        );
    }

    private ItemDTO convertItemToDTO(Item item) {
        return new ItemDTO(
                item.getTitle(),
                item.getDescription(),
                item.getImage(),
                item.getPrice(),
                item.getStock()
        );
    }

    private CustomerDTO convertCustomerToDTO(Customer customer) {
        return new CustomerDTO(
                customer.getUsername(),
                customer.getEmail(),
                customer.getPassword(),
                customer.getCreatedAt(),
                customer.getAddresses().stream()
                        .map(this::convertAddressToDTO)
                        .collect(Collectors.toList())
        );
    }

    private AddressDTO convertAddressToDTO(Address address) {
        return new AddressDTO(
                address.getStreet(),
                address.getCity(),
                address.getCounty(),
                address.getPostCode(),
                address.getCountry()
        );
    }
}
