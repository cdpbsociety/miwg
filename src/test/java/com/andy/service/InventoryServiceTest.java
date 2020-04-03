package com.andy.service;

import com.andy.data.Inventory;
import com.andy.data.Item;
import com.andy.exception.NotFoundException;
import com.andy.repository.InventoryRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InventoryServiceTest {

    @MockBean
    private InventoryRepository inventoryRepository;

    @Autowired
    private InventoryService inventoryService;

    private List<Inventory> mockInventories;

    @Before
    public void setup() {
        this.mockInventories = Arrays.asList(new Inventory(new Item("test1", "test1 desc", 55), 1));
        Mockito.when(inventoryRepository.selectAllItems()).thenReturn(mockInventories);
    }

    @Test
    public void getInventories_success() {
        List<Inventory> inventories = inventoryService.getInventories();
        Assert.assertEquals(mockInventories, inventories);
    }

    @Test
    public void buy_success() {
        Inventory inv = inventoryService.buy("test1");
        Assert.assertEquals(mockInventories.get(0), inv);
    }

    @Test(expected = NotFoundException.class)
    public void buy_failNotFound() {
        inventoryService.buy("some unknown item");
    }

    @Test(expected = NotFoundException.class)
    public void buy_failOutOfStock() {
        Inventory inv = inventoryService.buy("test1");
        Assert.assertEquals(mockInventories.get(0), inv);

        inventoryService.buy("test1");
    }

    @Test(expected = RuntimeException.class)
    public void buy_failDuplicateItem() {
        List<Inventory> mockData = Arrays.asList(
                new Inventory(new Item("test2", "test1 desc", 55), 1),
                new Inventory(new Item("test2", "test1 desc", 453), 155)
                );
        Mockito.when(inventoryRepository.selectAllItems()).thenReturn(mockData);
        inventoryService.buy("test2");
    }

}
