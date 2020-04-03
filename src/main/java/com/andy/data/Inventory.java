package com.andy.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Inventory {
    private Item item;
    private int numberAvailable;
}
