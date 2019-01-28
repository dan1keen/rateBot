package com.example.demo.service;


import com.example.demo.model.Item;
import com.example.demo.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ItemServiceImpl {
    private ItemRepository itemRepository;



    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Iterable<Item> list() {
        return itemRepository.findAll();
    }

    public Item save(Item items) {
            return itemRepository.save(items);
    }


    public Item getItemById(int id){
       return itemRepository.findItemById(1);
    }

    public Item getLastItem(){

        return itemRepository.findTopByOrderByIdDesc();
    }


    public Long exists(){
        return itemRepository.count();
    }
    public Iterable<Item> saveAll(Iterable<Item> items){
        return itemRepository.saveAll(items);
    }

    public Item getLastByName(String name){
        return itemRepository.findTopByNameOrderByIdDesc(name);
    }
}
