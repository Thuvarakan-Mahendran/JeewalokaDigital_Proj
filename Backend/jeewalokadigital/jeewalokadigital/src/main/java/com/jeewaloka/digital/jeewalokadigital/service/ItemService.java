package com.jeewaloka.digital.jeewalokadigital.service;

import com.jeewaloka.digital.jeewalokadigital.dto.ItemDTO;

import java.util.List;

public interface ItemService {
    List<ItemDTO> getAllItems();
    ItemDTO saveItem(ItemDTO itemDTO);
    ItemDTO editItem(ItemDTO itemDTO);
    String deleteItem(ItemDTO itemDTO);
}
