package com.andy.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Item {
    public static final double SURGE_PRICE_INCREASE = 1.10;
    private String name;
    private String description;
    private Integer price;

    @JsonIgnore
    public double getSurgePrice() {
        return SURGE_PRICE_INCREASE * price;
    }
}
