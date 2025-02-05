package com.jeewaloka.digital.jeewalokadigital.service;

import com.jeewaloka.digital.jeewalokadigital.dto.ItemDTO;
import com.jeewaloka.digital.jeewalokadigital.entity.Item;
import com.jeewaloka.digital.jeewalokadigital.entity.Supplier;
import com.jeewaloka.digital.jeewalokadigital.repository.ItemRepository;
import com.jeewaloka.digital.jeewalokadigital.repository.SupplierRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private ModelMapper modelMapper;

    public ItemDTO saveItem(ItemDTO itemDTO) {
        Supplier supplier = supplierRepository.findById(itemDTO.getSupplierId())
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        Item item = modelMapper.map(itemDTO, Item.class);
        item.setSupplier(supplier);

        item = itemRepository.save(item);
        return modelMapper.map(item, ItemDTO.class);
    }

    public ItemDTO editItem(Long id, ItemDTO itemDTO) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        Supplier supplier = supplierRepository.findById(itemDTO.getSupplierId())
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        modelMapper.map(itemDTO, item);
        item.setSupplier(supplier);

        item = itemRepository.save(item);
        return modelMapper.map(item, ItemDTO.class);
    }

    public String deleteItem(Long id) {
        if (!itemRepository.existsById(id)) {
            throw new RuntimeException("Item not found");
        }
        itemRepository.deleteById(id);
        return "Item deleted successfully";
    }

    public List<ItemDTO> getAllItems() {
        List<Item> items = itemRepository.findAll();
        return modelMapper.map(items, new TypeToken<List<ItemDTO>>() {}.getType());
    }
}
