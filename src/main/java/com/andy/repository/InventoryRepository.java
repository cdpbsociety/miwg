package com.andy.repository;

import com.andy.data.Inventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class InventoryRepository {
    private List<Inventory> inventoryDB;

    @Autowired
    public InventoryRepository(ItemRepository itemRepository) {
        this.inventoryDB = itemRepository.selectAllItems().stream().map(item -> {
            return new Inventory(item, 10);
        }).collect(Collectors.toList());

    }

    public List<Inventory> selectAllItems() {
        return inventoryDB;
    }
}
