package com.jeewaloka.digital.jeewalokadigital.service;

import com.jeewaloka.digital.jeewalokadigital.dto.ItemDTO;
import com.jeewaloka.digital.jeewalokadigital.entity.Item;
import com.jeewaloka.digital.jeewalokadigital.repository.ItemRepo;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ItemService {

    @Autowired
    private ItemRepo itemRepo;
    @Autowired
    private ModelMapper modelMapper;

    public List<ItemDTO> getAllItems() {
        List<Item> itemList = itemRepo.findAll();
        return modelMapper.map(itemList, new TypeToken<List<ItemDTO>>() {
        }.getType());
    }

    public ItemDTO saveItem(ItemDTO itemDTO) {
        itemRepo.save(modelMapper.map(itemDTO, Item.class));
        return itemDTO;
    }

    public ItemDTO editItem(ItemDTO itemDTO) {
        itemRepo.save(modelMapper.map(itemDTO, Item.class));
        return itemDTO;
    }

    public String deleteItem(ItemDTO itemDTO) {
        itemRepo.delete(modelMapper.map(itemDTO, Item.class));
        return "Item deleted";
    }

}
