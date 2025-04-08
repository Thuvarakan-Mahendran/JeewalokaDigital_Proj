package com.jeewaloka.digital.jeewalokadigital.controller;

import com.jeewaloka.digital.jeewalokadigital.util.ApiResponse;
import com.jeewaloka.digital.jeewalokadigital.dto.Request.PurchaseOrderDTO;
import com.jeewaloka.digital.jeewalokadigital.dto.Response.PurchaseOrderResponseDTO;
import com.jeewaloka.digital.jeewalokadigital.entity.PurchaseOrder;
import com.jeewaloka.digital.jeewalokadigital.service.PurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchaseorder")
//@CrossOrigin

public class PurchaseOrderController {

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    // Create a new GRN
    @PostMapping("/createpurchaseorder")
    public ResponseEntity<ApiResponse<PurchaseOrder>> createPO(@RequestBody PurchaseOrderDTO purchaseOrderDTO) {
        try {
            PurchaseOrder createPO = purchaseOrderService.createPO(purchaseOrderDTO);
            ApiResponse<PurchaseOrder> response = new ApiResponse<>(
                    HttpStatus.CREATED.value(),
                    "PO created successfully.",
                    createPO
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            ApiResponse<PurchaseOrder> errorResponse = new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Failed to create PO: " + e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


    @GetMapping("getpurchaseorder/{id}")
    public ResponseEntity<ApiResponse<PurchaseOrderResponseDTO>> getPO(@PathVariable Long id) {
        try {
            PurchaseOrderResponseDTO purchaseOrderResponse = purchaseOrderService.getPurchaseOrderById(id);
            ApiResponse<PurchaseOrderResponseDTO> response = new ApiResponse<>(
                    HttpStatus.OK.value(),
                    "PO fetched successfully.",
                    purchaseOrderResponse
            );
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            ApiResponse<PurchaseOrderResponseDTO> errorResponse = new ApiResponse<>(
                    HttpStatus.NOT_FOUND.value(),
                    "PO not found: " + e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }
    @GetMapping("/getallpurchaseorders")
    public ResponseEntity<ApiResponse<List<PurchaseOrderResponseDTO>>> getAllPOs() {
        try {
            List<PurchaseOrderResponseDTO> purchaseOrders = purchaseOrderService.getAllPOs();
            ApiResponse<List<PurchaseOrderResponseDTO>> response = new ApiResponse<>(
                    HttpStatus.OK.value(),
                    "All POs fetched successfully.",
                    purchaseOrders
            );
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            ApiResponse<List<PurchaseOrderResponseDTO>> errorResponse = new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Failed to fetch POs: " + e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PutMapping("editpurchaseorder/{id}")
    public ResponseEntity<ApiResponse<PurchaseOrder>> updatePO(@PathVariable Long id, @RequestBody PurchaseOrderDTO purchaseOrderDTO) {
        try {
            PurchaseOrder updatedPO = purchaseOrderService.updatePO(id, purchaseOrderDTO);
            ApiResponse<PurchaseOrder> response = new ApiResponse<>(
                    HttpStatus.OK.value(),
                    "PO updated successfully.",
                    null
            );
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            ApiResponse<PurchaseOrder> errorResponse = new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Failed to update PO: " + e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    @DeleteMapping("deletepurchaseorder/{id}")

    public ResponseEntity<ApiResponse<Void>> deletePO(@PathVariable Long id) {
        try {
            purchaseOrderService.deletePO(id);
            ApiResponse<Void> response = new ApiResponse<>(
                    HttpStatus.NO_CONTENT.value(),
                    "PO deleted successfully.",
                    null
            );
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
        } catch (Exception e) {
            ApiResponse<Void> errorResponse = new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Failed to delete PO: " + e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

}
