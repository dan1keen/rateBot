package com.example.demo.controller;

import com.example.demo.model.Item;
import com.example.demo.service.ItemServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ItemController {

    private ItemServiceImpl itemService;
    @Autowired
    public void setItemService(ItemServiceImpl itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/list")
    public Iterable<Item> list() {
        return itemService.list();
    }
}
