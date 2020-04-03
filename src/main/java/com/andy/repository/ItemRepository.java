package com.andy.repository;

import com.andy.data.Item;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ItemRepository {
    private List<Item> itemsDB;

    public ItemRepository() {
        itemsDB = new ArrayList<Item>(
                List.of(
                        new Item("Ale", "A fine pale ale", 20),
                        new Item("Lager", "A light lager", 25),
                        new Item("WhiteWine", "An excellent white wine", 50),
                        new Item("RedWine", "The best red wine", 60),
                        new Item("AgedCheddar", "5 year old aged cheddar cheese", 15),
                        new Item("BLT", "Bacon, Lettuce and Tomato Sandwich", 18)
                ));
    }

    public List<Item> selectAllItems() {
        return this.itemsDB;
    }
}
