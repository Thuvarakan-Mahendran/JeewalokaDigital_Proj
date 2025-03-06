package com.jeewaloka.digital.jeewalokadigital.service.impl;

import com.jeewaloka.digital.jeewalokadigital.dto.GRRNDTO;
import com.jeewaloka.digital.jeewalokadigital.dto.ItemDTO;
import com.jeewaloka.digital.jeewalokadigital.entity.GRRN;
import com.jeewaloka.digital.jeewalokadigital.entity.GRRNItem;
import com.jeewaloka.digital.jeewalokadigital.entity.Item;
import com.jeewaloka.digital.jeewalokadigital.repository.GRRNItemRepository;
import com.jeewaloka.digital.jeewalokadigital.repository.GRRNRepository;
import com.jeewaloka.digital.jeewalokadigital.repository.ItemRepository;
import com.jeewaloka.digital.jeewalokadigital.service.GRRNService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GRRNServiceImpl implements GRRNService {
    @Autowired
    private GRRNRepository grrnRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private GRRNItemRepository grrnItemRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Override
    public GRRNDTO addGRRNWithItems(GRRNDTO grrnDTO) {
        // Convert GRRNDTO to GRRN entity
        GRRN grrn = modelMapper.map(grrnDTO, GRRN.class);

        // Save the GRRN entity to the repository
        grrn = grrnRepository.save(grrn);

        // Convert the GRRNItemDTO list to GRRNItem entities
        GRRN finalGrrn = grrn;
        List<GRRNItem> grrnItems = grrnDTO.getGrrnItemList().stream()
                .map(grrnItemDTO -> {
                    GRRNItem grrnItem = modelMapper.map(grrnItemDTO, GRRNItem.class);
                    grrnItem.setGrn(finalGrrn); // Set the GRRN entity in GRRNItem
                    return grrnItem;
                }).collect(Collectors.toList());



        // Save all GRRNItem entities to the repository
        grrnItemRepository.saveAll(grrnItems);

        // Map the saved GRRN entity back to GRRNDTO and return it
        return modelMapper.map(grrn, GRRNDTO.class);
    }

    @Override
    public ItemDTO getItemByName(String itemName) {
        // Fetch the item from the repository using the item name
        try {
            Item item = itemRepository.findByItemNameIgnoreCase(itemName); // Adjust method based on your repository
            if (item == null) {
                return null; // Item not found
            }

            // Map entity to DTO and return it
            ItemDTO itemDTO = new ItemDTO();
            itemDTO.setItemCode(item.getItemCode());
            itemDTO.setItemName(item.getItemName());
            itemDTO.setItemType(item.getItemType());
            itemDTO.setItemPurchasePrice(item.getItemPurchasePrice());
            itemDTO.setItemSalesPrice(item.getItemSalesPrice());
            itemDTO.setSupplierId(item.getSupplier().getSupplierId());
            itemDTO.setStatus(item.getStatus());

            return itemDTO;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteGRRN(Long grrnId) {
        // Fetch the GRRN entity by its ID
        GRRN grrn = grrnRepository.findById(grrnId)
                .orElseThrow(() -> new RuntimeException("GRRN not found"));

        // Delete associated GRRNItems
        grrnItemRepository.deleteAll(grrn.getGrrnItemList()); // Delete all related items

        // Delete the GRRN entity itself
        grrnRepository.delete(grrn);
    }

    @Override
    public GRRNDTO getGRRNById(Long grrnId) {
        // Fetch the GRRN entity by ID
        GRRN grrn = grrnRepository.findById(grrnId)
                .orElseThrow(() -> new RuntimeException("GRRN not found"));

        // Convert the GRRN entity to GRRNDTO (which includes the list of GRRNItem)
        GRRNDTO grrnDTO = modelMapper.map(grrn, GRRNDTO.class);
        return grrnDTO;
    }


}
