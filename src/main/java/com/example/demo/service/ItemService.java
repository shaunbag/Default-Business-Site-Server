package com.example.demo.service;

import com.example.demo.dto.ItemDTO;
import com.example.demo.model.Item;
import com.example.demo.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    public List<ItemDTO> getAll() {
        return itemRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<ItemDTO> getById(Long id) {
        return itemRepository.findById(id)
                .map(this::convertToDTO);
    }

    public ItemDTO create(ItemDTO dto) {
        Item item = new Item(
                dto.title(),
                dto.description(),
                dto.image(),
                dto.price(),
                dto.stock()
        );
        Item saved = itemRepository.save(item);
        return convertToDTO(saved);
    }

    public Optional<ItemDTO> update(Long id, ItemDTO dto) {
        Optional<Item> existing = itemRepository.findById(id);
        if (existing.isPresent()) {
            Item item = existing.get();
            item.setTitle(dto.title());
            item.setDescription(dto.description());
            item.setImage(dto.image());
            item.setPrice(dto.price());
            item.setStock(dto.stock());
            Item updated = itemRepository.save(item);
            return Optional.of(convertToDTO(updated));
        }
        return Optional.empty();
    }

    public boolean delete(Long id) {
        if (itemRepository.existsById(id)) {
            itemRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private ItemDTO convertToDTO(Item item) {
        return new ItemDTO(
                item.getTitle(),
                item.getDescription(),
                item.getImage(),
                item.getPrice(),
                item.getStock()
        );
    }
}
