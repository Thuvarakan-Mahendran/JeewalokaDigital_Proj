package com.jeewaloka.digital.jeewalokadigital.service;

import com.jeewaloka.digital.jeewalokadigital.dto.Request.GRNRequestDTO;
import com.jeewaloka.digital.jeewalokadigital.dto.Request.GRNItemRequestDTO;
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
    public GRN createGRN(GRNRequestDTO grnDTO) {
        Supplier supplier = supplierRepository.findById(grnDTO.getGrnSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));

        GRN grn = new GRN();
        grn.setGrnReceivedBy(grnDTO.getGrnReceivedBy());
        grn.setGrnSupplier(supplier);
        grn.setGrnStatus(grnDTO.getGrnStatus());
        grn.setGrnItems(new ArrayList<>());

        double totalAmount = 0.0; // Initialize total amount

        for (GRNItemRequestDTO grnItemDTO : grnDTO.getGrnItems()) {
            Item item = itemRepository.findById(grnItemDTO.getItemId())
                    .orElseThrow(() -> new ResourceNotFoundException("Item not found"));

            GRNItem grnItem = new GRNItem();
            grnItem.setQuantity(grnItemDTO.getQuantity());
            grnItem.setUnitPrice(grnItemDTO.getUnitPrice());

            // Calculate totalAmount for each GRNItem
            double itemTotal = grnItemDTO.getQuantity() * grnItemDTO.getUnitPrice();
            grnItem.setTotalAmount(itemTotal);

            grnItem.setItemExpiryDate(grnItemDTO.getItemExpiryDate());
            grnItem.setItemManufactureDate(grnItemDTO.getItemManufactureDate());
            grnItem.setItem(item);
            grnItem.setGrn(grn);

            grn.getGrnItems().add(grnItem);
            totalAmount += itemTotal; // Accumulate total amount
        }

        grn.setGrnTotalAmount(totalAmount); // Set total amount after calculation

        return grnRepository.save(grn);
    }

    public GRNResponseDTO getGRNById(Long id) {
        GRN grn = grnRepository.findById(id).orElseThrow(() -> new RuntimeException("GRN not found"));

        GRNResponseDTO grnResponseDTO = new GRNResponseDTO();
        grnResponseDTO.setGrnId(grn.getGrnId());
        grnResponseDTO.setGrnReceivedBy(grn.getGrnReceivedBy());
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
    public GRN updateGRN(Long id, GRNRequestDTO grnDTO) {
        GRN existingGRN = grnRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("GRN not found with id: " + id));

        existingGRN.setGrnReceivedBy(grnDTO.getGrnReceivedBy());
        existingGRN.setGrnStatus(grnDTO.getGrnStatus());

        Supplier supplier = supplierRepository.findById(grnDTO.getGrnSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));
        existingGRN.setGrnSupplier(supplier);

        existingGRN.getGrnItems().clear(); // Remove existing items

        double totalAmount = 0.0;

        for (GRNItemRequestDTO grnItemDTO : grnDTO.getGrnItems()) {
            Item item = itemRepository.findById(grnItemDTO.getItemId())
                    .orElseThrow(() -> new ResourceNotFoundException("Item not found"));

            GRNItem grnItem = new GRNItem();
            grnItem.setQuantity(grnItemDTO.getQuantity());
            grnItem.setUnitPrice(grnItemDTO.getUnitPrice());

            double itemTotal = grnItemDTO.getQuantity() * grnItemDTO.getUnitPrice();
            grnItem.setTotalAmount(itemTotal);

            grnItem.setItemExpiryDate(grnItemDTO.getItemExpiryDate());
            grnItem.setItemManufactureDate(grnItemDTO.getItemManufactureDate());
            grnItem.setItem(item);
            grnItem.setGrn(existingGRN);

            existingGRN.getGrnItems().add(grnItem);
            totalAmount += itemTotal;
        }

        existingGRN.setGrnTotalAmount(totalAmount);

        return grnRepository.save(existingGRN);
    }


    // Delete GRN (same as before)
    public void deleteGRN(Long id) {
        GRN grn = grnRepository.findById(id).orElseThrow(() -> new RuntimeException("GRN not found"));
        grnRepository.delete(grn);
    }
}
