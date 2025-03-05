package com.jeewaloka.digital.jeewalokadigital.mapper;

import com.jeewaloka.digital.jeewalokadigital.dto.Request.BillItemRequestDTO;
import com.jeewaloka.digital.jeewalokadigital.dto.Response.BillItemResponseDTO;
import com.jeewaloka.digital.jeewalokadigital.entity.Item;
import com.jeewaloka.digital.jeewalokadigital.entity.bill.BillItem;
import com.jeewaloka.digital.jeewalokadigital.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BillItemMapper {
    @Autowired
    private ItemRepository itemRepository;
    public BillItem toBillItem(BillItemRequestDTO billItemDTO) {
        if(billItemDTO == null){
            return null;
        }
        BillItem billItem = new BillItem();
        billItem.setTotalValue(billItemDTO.getTotalValue());
        billItem.setQuantity(billItemDTO.getQuantity());
        if(billItemDTO.getItem() != null){
            Item item = itemRepository.findById(billItemDTO.getItem())
                    .orElseThrow(() -> new EntityNotFoundException("Item not found with ID: "+ billItemDTO.getItem()));
            billItem.setItem(item);
        }
        return billItem;
    }

    public BillItemResponseDTO toBillItemDTO(BillItem billItem) {
        if(billItem == null){
            return null;
        }
        BillItemResponseDTO billItemResponseDTO = new BillItemResponseDTO();
        billItemResponseDTO.setBIID(billItem.getBIID());
        billItemResponseDTO.setQuantity(billItem.getQuantity());
        billItemResponseDTO.setTotalValue(billItem.getTotalValue());
        return billItemResponseDTO;
    }
}
