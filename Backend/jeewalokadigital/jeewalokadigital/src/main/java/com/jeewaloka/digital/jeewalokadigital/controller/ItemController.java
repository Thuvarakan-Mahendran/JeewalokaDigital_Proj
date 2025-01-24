package com.jeewaloka.digital.jeewalokadigital.controller;

import com.jeewaloka.digital.jeewalokadigital.dto.ItemDTO;
import com.jeewaloka.digital.jeewalokadigital.service.impl.ItemServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "api/items")

public class ItemController {
    @Autowired
    private ItemServiceImpl itemService;

    @GetMapping("/getitems")
    public List<ItemDTO> getItems() {
        return itemService.getAllItems();
    }

    @PostMapping("/saveitem")
    public ItemDTO saveItem(@RequestBody ItemDTO itemDTO) {
        return itemService.saveItem(itemDTO);
    }

    @PutMapping("/edititem")
    public ItemDTO editItem(@RequestBody ItemDTO itemDTO){
        return itemService.editItem(itemDTO);
    }
    @DeleteMapping("/deleteitem")
    public String deleteItem(@RequestBody ItemDTO itemDTO){
        return itemService.deleteItem(itemDTO);

    }
}
