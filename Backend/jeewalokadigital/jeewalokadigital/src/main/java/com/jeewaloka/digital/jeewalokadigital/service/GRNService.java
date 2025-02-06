package com.jeewaloka.digital.jeewalokadigital.service;

import com.jeewaloka.digital.jeewalokadigital.dto.Request.GRNDTO;
import com.jeewaloka.digital.jeewalokadigital.dto.Request.GRNItemDTO;
import com.jeewaloka.digital.jeewalokadigital.dto.Response.GRNItemResponseDTO;
import com.jeewaloka.digital.jeewalokadigital.dto.Response.GRNResponseDTO;
import com.jeewaloka.digital.jeewalokadigital.entity.GRN;
import com.jeewaloka.digital.jeewalokadigital.entity.GRNItem;
import com.jeewaloka.digital.jeewalokadigital.entity.Item;
import com.jeewaloka.digital.jeewalokadigital.entity.Supplier;
import com.jeewaloka.digital.jeewalokadigital.repository.GRNRepository;
import com.jeewaloka.digital.jeewalokadigital.repository.ItemRepository;
import com.jeewaloka.digital.jeewalokadigital.repository.SupplierRepository;
import jakarta.transaction.Transactional;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GRNService {

    @Autowired
    private GRNRepository grnRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private ItemRepository itemRepository;  // Add ItemRepository to fetch Items

    @Transactional
    public GRN createGRN(GRNDTO grnDTO) {
        // Fetch the Supplier from the database
        Supplier supplier = supplierRepository.findById(grnDTO.getGrnSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));

        GRN grn = new GRN();
        grn.setGrnReceivedBy(grnDTO.getGrnReceivedBy());
        grn.setGrnTotalAmount(grnDTO.getGrnTotalAmount());
        grn.setGrnStatus(grnDTO.getGrnStatus());
        grn.setGrnSupplier(supplier);  // Set the full Supplier entity
        grn.setGrnItems(new ArrayList<>()); // Initialize list

        // Loop through GRNItems and set Item for each
        for (GRNItemDTO grnItemDTO : grnDTO.getGrnItems()) {
            // Fetch the Item from the database using the itemId
            Item item = itemRepository.findById(grnItemDTO.getItemId())
                    .orElseThrow(() -> new ResourceNotFoundException("Item not found"));

            GRNItem grnItem = new GRNItem();
            grnItem.setQuantity(grnItemDTO.getQuantity());
            grnItem.setUnitPrice(grnItemDTO.getUnitPrice());
            grnItem.setTotalAmount(grnItemDTO.getTotalAmount());
            grnItem.setItemExpiryDate(grnItemDTO.getItemExpiryDate());
            grnItem.setItemManufactureDate(grnItemDTO.getItemManufactureDate());
            grnItem.setItem(item);  // Set the item to the GRNItem
            grnItem.setGrn(grn);  // Associate GRN with this item
            grn.getGrnItems().add(grnItem);
        }

        return grnRepository.save(grn);  // Save GRN and its items
    }
    public GRNResponseDTO getGRNById(Long id) {
        GRN grn = grnRepository.findById(id).orElseThrow(() -> new RuntimeException("GRN not found"));

        GRNResponseDTO grnResponseDTO = new GRNResponseDTO();
        grnResponseDTO.setGrnId(grn.getGrnId());
        grnResponseDTO.setGrnReceivedBy(grn.getGrnReceivedBy());
        grnResponseDTO.setGrnTotalAmount(grn.getGrnTotalAmount());
        grnResponseDTO.setGrnStatus(grn.getGrnStatus());
        grnResponseDTO.setGrnSupplierName(grn.getGrnSupplier().getSupplierName());

        // Add GRNItems to the response
        List<GRNItemResponseDTO> grnItemResponseDTOs = grn.getGrnItems().stream().map(grnItem -> {
            GRNItemResponseDTO grnItemResponseDTO = new GRNItemResponseDTO();
            grnItemResponseDTO.setItemId(grnItem.getItem().getItemCode());
            return getGrnItemResponseDTO(grnItem, grnItemResponseDTO);
        }).collect(Collectors.toList());

        grnResponseDTO.setGrnItems(grnItemResponseDTOs);
        return grnResponseDTO;
    }

    private GRNItemResponseDTO getGrnItemResponseDTO(GRNItem grnItem, GRNItemResponseDTO grnItemResponseDTO) {
        grnItemResponseDTO.setItemName(grnItem.getItem().getItemName());
        grnItemResponseDTO.setItemCode(String.valueOf(grnItem.getItem().getItemCode()));
        grnItemResponseDTO.setQuantity(grnItem.getQuantity());
        grnItemResponseDTO.setUnitPrice(grnItem.getUnitPrice());
        grnItemResponseDTO.setTotalAmount(grnItem.getTotalAmount());
        grnItemResponseDTO.setItemExpiryDate(grnItem.getItemExpiryDate());
        grnItemResponseDTO.setItemManufactureDate(grnItem.getItemManufactureDate());
        return grnItemResponseDTO;
    }

    // Get all GRNs
    public List<GRNResponseDTO> getAllGRNs() {
        List<GRN> grns = grnRepository.findAll();

        return grns.stream().map(grn -> {
            GRNResponseDTO grnResponseDTO = new GRNResponseDTO();
            grnResponseDTO.setGrnId(grn.getGrnId());
            grnResponseDTO.setGrnReceivedBy(grn.getGrnReceivedBy());
            grnResponseDTO.setGrnTotalAmount(grn.getGrnTotalAmount());
            grnResponseDTO.setGrnStatus(grn.getGrnStatus());
            grnResponseDTO.setGrnSupplierName(grn.getGrnSupplier().getSupplierName());

            // Add GRNItems to the response
            List<GRNItemResponseDTO> grnItemResponseDTOs = grn.getGrnItems().stream().map(grnItem -> {
                GRNItemResponseDTO grnItemResponseDTO = new GRNItemResponseDTO();
                return getGrnItemResponseDTO(grnItem, grnItemResponseDTO);
            }).collect(Collectors.toList());

            grnResponseDTO.setGrnItems(grnItemResponseDTOs);
            return grnResponseDTO;
        }).collect(Collectors.toList());
    }

    // Update GRN (same as before)
    @Transactional
    public GRN updateGRN(Long id, GRNDTO grnDTO) {
        // Fetch the existing GRN from the database
        GRN existingGRN = grnRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("GRN not found with id: " + id));

        // Update basic fields of the GRN
        existingGRN.setGrnReceivedBy(grnDTO.getGrnReceivedBy());
        existingGRN.setGrnTotalAmount(grnDTO.getGrnTotalAmount());
        existingGRN.setGrnStatus(grnDTO.getGrnStatus());

        // Fetch and set the Supplier
        Supplier supplier = supplierRepository.findById(grnDTO.getGrnSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + grnDTO.getGrnSupplierId()));
        existingGRN.setGrnSupplier(supplier);

        // Remove existing GRN items before adding new ones
        existingGRN.getGrnItems().clear();

        for (GRNItemDTO grnItemDTO : grnDTO.getGrnItems()) {
            // Fetch the Item to associate with the GRNItem
            Item item = itemRepository.findById(grnItemDTO.getItemId())
                    .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + grnItemDTO.getItemId()));

            // Create a new GRNItem and set its properties
            GRNItem grnItem = new GRNItem();
            grnItem.setQuantity(grnItemDTO.getQuantity());
            grnItem.setUnitPrice(grnItemDTO.getUnitPrice());
            grnItem.setTotalAmount(grnItemDTO.getTotalAmount());
            grnItem.setItemExpiryDate(grnItemDTO.getItemExpiryDate());
            grnItem.setItemManufactureDate(grnItemDTO.getItemManufactureDate());
            grnItem.setItem(item);
            grnItem.setGrn(existingGRN);  // Set the GRN this item belongs to

            // Add the new GRNItem to the GRN
            existingGRN.getGrnItems().add(grnItem);
        }

        // Save and return the updated GRN
        return grnRepository.save(existingGRN);
    }


    // Delete GRN (same as before)
    public void deleteGRN(Long id) {
        GRN grn = grnRepository.findById(id).orElseThrow(() -> new RuntimeException("GRN not found"));
        grnRepository.delete(grn);
    }
}
