package com.jeewaloka.digital.jeewalokadigital.service;

import com.jeewaloka.digital.jeewalokadigital.dto.Request.PurchaseOrderItemDTO;
import com.jeewaloka.digital.jeewalokadigital.dto.Request.PurchaseOrderDTO;
import com.jeewaloka.digital.jeewalokadigital.dto.Response.PurchaseOrderItemResponseDTO;
import com.jeewaloka.digital.jeewalokadigital.dto.Response.PurchaseOrderResponseDTO;
import com.jeewaloka.digital.jeewalokadigital.entity.*;
import com.jeewaloka.digital.jeewalokadigital.repository.ItemRepository;
import com.jeewaloka.digital.jeewalokadigital.repository.PurchaseOrderRepository;
import com.jeewaloka.digital.jeewalokadigital.repository.SupplierRepository;
import jakarta.transaction.Transactional;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PurchaseOrderService {

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Transactional
    public PurchaseOrder createPO(PurchaseOrderDTO purchaseOrderDTO) {
        // Fetch the Supplier from the database using supplierId only
        Supplier supplier = supplierRepository.findById(purchaseOrderDTO.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));

        // Create a new Purchase Order
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setOrderDate(purchaseOrderDTO.getOrderDate());
        purchaseOrder.setStatus(purchaseOrderDTO.getStatus());
        purchaseOrder.setSupplier(supplier); // Use the fetched supplier
        purchaseOrder.setRemarks(purchaseOrderDTO.getRemarks());

        List<PurchaseOrderItem> poItems = new ArrayList<>();

        // Loop through DTO items and convert them to PO items
        for (var purchaseOrderItemDTO : purchaseOrderDTO.getPItems()) {
            // Fetch the Item from the database using only itemId
            Item item = itemRepository.findById(purchaseOrderItemDTO.getItemId())
                    .orElseThrow(() -> new ResourceNotFoundException("Item not found"));

            PurchaseOrderItem purchaseOrderItem = new PurchaseOrderItem();
            purchaseOrderItem.setItem(item);
            purchaseOrderItem.setQuantity(purchaseOrderItemDTO.getQuantity());
            purchaseOrderItem.setPurchaseOrder(purchaseOrder);
            poItems.add(purchaseOrderItem);
        }

        purchaseOrder.setItems(poItems);

        return purchaseOrderRepository.save(purchaseOrder);  // Save PO and its items
    }


    public PurchaseOrderResponseDTO getPurchaseOrderById(Long id){

        PurchaseOrder purchaseOrder=purchaseOrderRepository.findById(id).orElseThrow(()-> new RuntimeException("PurchaseOrder not found"));

        PurchaseOrderResponseDTO purchaseOrderResponseDTO=new PurchaseOrderResponseDTO();

        purchaseOrderResponseDTO.setPoId(purchaseOrder.getPoId());
        purchaseOrderResponseDTO.setSupplierName(purchaseOrderResponseDTO.getSupplierName());
        purchaseOrderResponseDTO.setOrderDate(purchaseOrder.getOrderDate());
        purchaseOrderResponseDTO.setStatus(purchaseOrder.getStatus());

        List<PurchaseOrderItemResponseDTO> purchaseOrderItemResponseDTOS=purchaseOrder.getItems().stream().map(purchaseOrderItem -> {

            PurchaseOrderItemResponseDTO purchaseOrderItemResponseDTO=new PurchaseOrderItemResponseDTO();
            purchaseOrderItemResponseDTO.setItemCode(purchaseOrderItem.getItem().getItemCode());
            return getPurchaseOrderItemResponseDTO(purchaseOrderItem,purchaseOrderItemResponseDTO);
                }).collect(Collectors.toList());
            purchaseOrderResponseDTO.setPoItems(purchaseOrderItemResponseDTOS);
            return  purchaseOrderResponseDTO;
    }

    private PurchaseOrderItemResponseDTO getPurchaseOrderItemResponseDTO(PurchaseOrderItem purchaseOrderItem,PurchaseOrderItemResponseDTO purchaseOrderItemResponseDTO){
        purchaseOrderItemResponseDTO.setItemName(purchaseOrderItem.getItem().getItemName());
        purchaseOrderItemResponseDTO.setItemCode(Long.valueOf(String.valueOf(purchaseOrderItem.getItem().getItemCode())));
        purchaseOrderItemResponseDTO.setQuantity(purchaseOrderItem.getQuantity());

        return purchaseOrderItemResponseDTO;
    }

    public List<PurchaseOrderResponseDTO> getAllPOs(){
        List<PurchaseOrder> purchaseOrders=purchaseOrderRepository.findAll();

        return purchaseOrders.stream().map(purchaseOrder -> {
            PurchaseOrderResponseDTO purchaseOrderResponseDTO=new PurchaseOrderResponseDTO();
            purchaseOrderResponseDTO.setPoId(purchaseOrder.getPoId());
            purchaseOrderResponseDTO.setSupplierName(purchaseOrder.getSupplier().getSupplierName());
            purchaseOrderResponseDTO.setOrderDate(purchaseOrder.getOrderDate());

            List<PurchaseOrderItemResponseDTO> purchaseOrderItemResponseDTOS=purchaseOrder.getItems().stream().map(purchaseOrderItem -> {
                PurchaseOrderItemResponseDTO purchaseOrderItemResponseDTO=new PurchaseOrderItemResponseDTO();
                return getPurchaseOrderItemResponseDTO(purchaseOrderItem,purchaseOrderItemResponseDTO);
            }).collect(Collectors.toList());

            purchaseOrderResponseDTO.setPoItems(purchaseOrderItemResponseDTOS);
            return purchaseOrderResponseDTO;
        }).collect(Collectors.toList());

    }
    @Transactional
    public PurchaseOrder updatePO(Long id, PurchaseOrderDTO purchaseOrderDTO) {
        // Fetch the existing PO from the database
        PurchaseOrder existingPurchaseOrder = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PO not found with id: " + id));

        // Update basic fields of the PO

        existingPurchaseOrder.setOrderDate(purchaseOrderDTO.getOrderDate());
        existingPurchaseOrder.setStatus(purchaseOrderDTO.getStatus());
        existingPurchaseOrder.setRemarks(purchaseOrderDTO.getRemarks());

        Supplier supplier = supplierRepository.findById(purchaseOrderDTO.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + purchaseOrderDTO.getSupplierId()));
        existingPurchaseOrder.setSupplier(supplier);

        // 🛑 Get existing items as a Map for fast lookup
        Map<Long, PurchaseOrderItem> existingItemsMap = existingPurchaseOrder.getItems().stream()
                .collect(Collectors.toMap(item -> item.getItem().getItemCode(), item -> item));

        List<PurchaseOrderItem> itemsToRemove = new ArrayList<>();

        // 🛑 Modify collection in-place instead of replacing it
        Iterator<PurchaseOrderItem> iterator = existingPurchaseOrder.getItems().iterator();
        while (iterator.hasNext()) {
            PurchaseOrderItem existingItem = iterator.next();
            boolean found = purchaseOrderDTO.getPItems().stream()
                    .anyMatch(dto -> dto.getItemId().equals(existingItem.getItem().getItemCode()));

            if (!found) {
                // Mark item for removal if it's not in the updated list
                itemsToRemove.add(existingItem);
                iterator.remove();
            }
        }

        // 🛑 Add or update items
        for (PurchaseOrderItemDTO purchaseOrderItemDTO : purchaseOrderDTO.getPItems()) {
            Item item = itemRepository.findById(purchaseOrderItemDTO.getItemId())
                    .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + purchaseOrderItemDTO.getItemId()));

            if (existingItemsMap.containsKey(item.getItemCode())) {
                // Update existing item
                PurchaseOrderItem existingItem = existingItemsMap.get(item.getItemCode());
                existingItem.setQuantity(purchaseOrderItemDTO.getQuantity());
            } else {
                // Add new item
                PurchaseOrderItem newItem = new PurchaseOrderItem();
                newItem.setItem(item);
                newItem.setQuantity(purchaseOrderItemDTO.getQuantity());
                newItem.setPurchaseOrder(existingPurchaseOrder);
                existingPurchaseOrder.getItems().add(newItem);
            }
        }

        // Remove orphaned items
        itemsToRemove.forEach(existingPurchaseOrder.getItems()::remove);

        // Save and return the updated PO
        return purchaseOrderRepository.save(existingPurchaseOrder);
    }


    public void deletePO(Long id) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(id).orElseThrow(() -> new RuntimeException("PurchaseOrder not found"));
        purchaseOrderRepository.delete(purchaseOrder);
    }


}
