package com.example.demo.service;

import com.example.demo.dto.ItemDTO;
import com.example.demo.model.Item;
import com.example.demo.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;

    private Item testItem;
    private ItemDTO testItemDTO;

    @BeforeEach
    void setUp() {
        testItem = new Item(
                "Test Item",
                "Test Description",
                "test-image.jpg",
                new BigDecimal("29.99"),
                100L
        );
        testItem.setId(1L);

        testItemDTO = new ItemDTO(
                "Test Item",
                "Test Description",
                "test-image.jpg",
                new BigDecimal("29.99"),
                100L
        );
    }

    @Test
    void testGetAll() {
        when(itemRepository.findAll()).thenReturn(Arrays.asList(testItem));

        var result = itemService.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Item", result.get(0).title());
        verify(itemRepository, times(1)).findAll();
    }

    @Test
    void testGetByIdSuccess() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(testItem));

        var result = itemService.getById(1L);

        assertTrue(result.isPresent());
        assertEquals("Test Item", result.get().title());
        verify(itemRepository, times(1)).findById(1L);
    }

    @Test
    void testGetByIdNotFound() {
        when(itemRepository.findById(99L)).thenReturn(Optional.empty());

        var result = itemService.getById(99L);

        assertFalse(result.isPresent());
        verify(itemRepository, times(1)).findById(99L);
    }

    @Test
    void testCreate() {
        when(itemRepository.save(any(Item.class))).thenReturn(testItem);

        var result = itemService.create(testItemDTO);

        assertNotNull(result);
        assertEquals("Test Item", result.title());
        assertEquals(new BigDecimal("29.99"), result.price());
        assertEquals(100L, result.stock());
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void testUpdateSuccess() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(testItem));
        when(itemRepository.save(any(Item.class))).thenReturn(testItem);

        var result = itemService.update(1L, testItemDTO);

        assertTrue(result.isPresent());
        assertEquals("Test Item", result.get().title());
        verify(itemRepository, times(1)).findById(1L);
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void testUpdateNotFound() {
        when(itemRepository.findById(99L)).thenReturn(Optional.empty());

        var result = itemService.update(99L, testItemDTO);

        assertFalse(result.isPresent());
        verify(itemRepository, times(1)).findById(99L);
        verify(itemRepository, never()).save(any(Item.class));
    }

    @Test
    void testDeleteSuccess() {
        when(itemRepository.existsById(1L)).thenReturn(true);

        var result = itemService.delete(1L);

        assertTrue(result);
        verify(itemRepository, times(1)).existsById(1L);
        verify(itemRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteNotFound() {
        when(itemRepository.existsById(99L)).thenReturn(false);

        var result = itemService.delete(99L);

        assertFalse(result);
        verify(itemRepository, times(1)).existsById(99L);
        verify(itemRepository, never()).deleteById(any());
    }
}
