package com.andy.service;

import com.andy.data.Inventory;
import com.andy.exception.NotFoundException;
import com.andy.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryService {
    private InventoryRepository inventoryRepository;
    private List<Long> lastViewTimeMS;
    private boolean surgePricing;

    @Autowired
    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
        this.surgePricing = false;
        this.lastViewTimeMS = new ArrayList<>();
    }

    public List<Inventory> getInventories() {
        surgePricingCheck();
        List<Inventory> inventories = inventoryRepository.selectAllItems();
        return inventories;
    }

    public Inventory buy(String name) {
        List<Inventory> inventories = inventoryRepository.selectAllItems();
        List<Inventory> matchedInventories = inventories.stream()
                .filter(inv -> inv.getItem().getName().equals(name))
                .collect(Collectors.toList());
        if (matchedInventories.size() == 0) {
            throw new NotFoundException(String.format("Item %1$s cannot be found", name));
        }

        // This shouldn't happen but just in case.
        if (matchedInventories.size() > 1) {
            throw new RuntimeException(String.format("Error: More than 1 item %1$s found in the inventory", name));
        }

        Inventory inventory = matchedInventories.get(0);
        if (inventory.getNumberAvailable() <= 0) {
            throw new NotFoundException(String.format("There are no more item %1$s available in the inventory", name));
        }

        int numNowAvailable = inventory.getNumberAvailable() - 1;
        inventory.setNumberAvailable(numNowAvailable);

        return inventory;
    }

    public boolean isSurgePricing() {
        return surgePricing;
    }

    private void surgePricingCheck() {
        lastViewTimeMS.add(System.currentTimeMillis());
        long numViewsInLastHour = lastViewTimeMS.stream().filter(lastView -> (lastView - System.currentTimeMillis()) <= 3600000L).count();
        this.surgePricing = numViewsInLastHour >= 10;
        // probably should clean up old ones...
    }

}
