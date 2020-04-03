package com.andy.controller;

import com.andy.controller.response.ItemResponse;
import com.andy.controller.response.ItemsResponse;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTests {
    @Autowired
    private MockMvc mockMvc;

    private int itemsCallCount = 0;
    private Gson gson = new Gson();
    private static final String AUTH_HEADER_VALID = "BASIC YW5keTpwYXNzd29yZA==";

    private ResultActions performGet(String serviceName) throws Exception {
        return mockMvc.perform(get(serviceName)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print());
    }

    private ResultActions performGet(String serviceName, String authHeader) throws Exception {
        return mockMvc.perform(get(serviceName)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", authHeader))
                .andDo(print());
    }

    private ResultActions getItems() throws Exception {
        this.itemsCallCount++;
        return performGet("/items")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isNotEmpty())
                .andExpect(jsonPath("$.items").isArray());
    }

    @Test
    public void items() throws Exception {
        MvcResult result = getItems().andReturn();
        MockHttpServletResponse resp = result.getResponse();
        ItemsResponse itemsResp = gson.fromJson(resp.getContentAsString(), ItemsResponse.class);
        ItemResponse item = itemsResp.getItems().get(0);
        Assert.assertNotNull(itemsResp.getItems());
        Assert.assertNotNull(itemsResp.getItems().get(0).getPrice());
    }

    @Test
    public void buy_success() throws Exception {
        performGet("/items/buy/Ale", AUTH_HEADER_VALID)
                .andExpect(status().isOk());
    }

    @Test
    public void buy_itemNotFound() throws Exception {
        performGet("/items/buy/nothing", AUTH_HEADER_VALID)
                .andExpect(status().isNotFound());
    }

    @Test
    public void buyEmAll_itemNotFound() throws Exception {
        for (int i = 0; i < 20; i++) {
            performGet("/items/buy/BLT", AUTH_HEADER_VALID);
        }
        performGet("/items/buy/BLT", AUTH_HEADER_VALID)
                .andExpect(status().isNotFound());
    }

    @Test
    public void buy_unauthorized() throws Exception {
        // Not logged in
        performGet("/items/buy/Ale").andExpect(status().isUnauthorized());
        // Basic auth not used
        performGet("/items/buy/Ale", "BEARER dskskfjds").andExpect(status().isUnauthorized());
        // Invalid credentials
        performGet("/items/buy/Ale", "basic abcdefg").andExpect(status().isUnauthorized());
    }


    @Test
    public void buy_surgePricing() throws Exception {
        MvcResult result = performGet("/items/buy/Ale", AUTH_HEADER_VALID).andReturn();
        MockHttpServletResponse resp = result.getResponse();
        // The price before surge pricing
        ItemResponse itemOriginal = gson.fromJson(resp.getContentAsString(), ItemResponse.class);

        // Call view items 10 times
        int itemsCallCount = this.itemsCallCount;
        for (int i = itemsCallCount; i < 10; i++) {
            getItems();
        }
        result = performGet("/items/buy/Ale", AUTH_HEADER_VALID).andReturn();
        resp = result.getResponse();
        ItemResponse itemNow = gson.fromJson(resp.getContentAsString(), ItemResponse.class);

        Assert.assertNotEquals(itemOriginal.getPrice(), itemNow.getPrice());
    }

    @Test
    public void invalidUrl() throws Exception {
        performGet("/bsksdflfksdjsslk")
                .andExpect(status().isNotFound());
    }
}
