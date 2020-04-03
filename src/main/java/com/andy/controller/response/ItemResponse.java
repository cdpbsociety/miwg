package com.andy.controller.response;

import com.andy.data.Item;
import lombok.Data;

@Data
public class ItemResponse {
    private final String name;
    private final String description;
    private final double price;

    public ItemResponse(Item item, boolean surgePricing) {
        this.name = item.getName();
        this.description = item.getDescription();
        this.price = surgePricing ? item.getSurgePrice() : item.getPrice();
    }
}
