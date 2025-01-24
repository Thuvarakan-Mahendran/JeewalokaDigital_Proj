package com.jeewaloka.digital.jeewalokadigital.controller;

import com.jeewaloka.digital.jeewalokadigital.dto.ItemDTO;
import com.jeewaloka.digital.jeewalokadigital.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "api/items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping("/getitems")
    public List<ItemDTO> getItems() {
        return itemService.getAllItems();
    }

    @PostMapping("/saveitem")
    public ItemDTO saveItem(@RequestBody ItemDTO itemDTO) {
        return itemService.saveItem(itemDTO);
    }

    @PutMapping("/edititem/{id}")
    public ItemDTO editItem(@PathVariable Long id, @RequestBody ItemDTO itemDTO){
        return itemService.editItem(id, itemDTO);
    }

    @DeleteMapping("/deleteitem/{id}")
    public String deleteItem(@PathVariable Long id){
        return itemService.deleteItem(id);
    }
}
