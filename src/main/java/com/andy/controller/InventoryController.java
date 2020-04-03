package com.andy.controller;

import com.andy.controller.response.ItemResponse;
import com.andy.controller.response.ItemsResponse;
import com.andy.data.Inventory;
import com.andy.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/items")
public class InventoryController {
    private Logger log = LoggerFactory.getLogger(this.getClass());
    private final InventoryService inventoryService;

    @Autowired
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public ItemsResponse items() {
        log.debug("Getting a list of all items");
        List<Inventory> inventories = inventoryService.getInventories();
        // will never be null so I'm not checking it.
        List<ItemResponse> items = inventories.stream().map(inventory -> {
            return new ItemResponse(inventory.getItem(), false);
        }).collect(Collectors.toList());

        ItemsResponse response = new ItemsResponse(items);
        return response;
    }

    @GetMapping("/buy/{name}")
    public ItemResponse buy(@PathVariable @NotNull String name) {
        log.debug("Buying an item");
        Inventory inventory = inventoryService.buy(name);
        ItemResponse response = new ItemResponse(inventory.getItem(), inventoryService.isSurgePricing());
        return response;
     }
}
