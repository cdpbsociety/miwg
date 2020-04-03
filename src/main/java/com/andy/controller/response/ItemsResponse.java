package com.andy.controller.response;

import lombok.Data;

import java.util.List;

@Data
public class ItemsResponse {
    private final List<ItemResponse> items;
}
