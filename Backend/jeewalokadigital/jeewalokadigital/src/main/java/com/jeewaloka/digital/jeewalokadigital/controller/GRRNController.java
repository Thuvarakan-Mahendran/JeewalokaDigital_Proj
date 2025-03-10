package com.jeewaloka.digital.jeewalokadigital.controller;

import com.jeewaloka.digital.jeewalokadigital.dto.GRRNDTO;
import com.jeewaloka.digital.jeewalokadigital.dto.ItemDTO;
import com.jeewaloka.digital.jeewalokadigital.dto.Response.SupplierResponseDTO;
import com.jeewaloka.digital.jeewalokadigital.service.GRRNService;
import com.jeewaloka.digital.jeewalokadigital.service.SupplierService;
import com.jeewaloka.digital.jeewalokadigital.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/grrns")
@CrossOrigin
public class GRRNController {

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private GRRNService grrnService;

    // Get Supplier Info by Name
    @GetMapping("/getSupplierInfo/{supplierName}")
    public ResponseEntity<SupplierResponseDTO> getSupplierInfo(@PathVariable("supplierName") String supplierName) {
        try {
            SupplierResponseDTO supplierResponseDTO = supplierService.getSupplierByName(supplierName);
            if (supplierResponseDTO == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Supplier not found
            }
            return new ResponseEntity<>(supplierResponseDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // Handle unexpected errors
        }
    }

    // Get Item by Name for GRRN
    @GetMapping("/getitembyname/{itemName}")
    public ResponseEntity<ItemDTO> getItemByName(@PathVariable("itemName") String itemName) {
        try {
            ItemDTO itemDTO = grrnService.getItemByName(itemName);
            if (itemDTO == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Item not found
            }
            return new ResponseEntity<>(itemDTO, HttpStatus.OK); // Return item if found
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // Handle unexpected errors
        }
    }

    // POST endpoint to save GRRN with GRRNItems
    @PostMapping("/savegrrnwithitems")
    public ResponseEntity<ApiResponse<GRRNDTO>> createGRRNWithItems(@RequestBody GRRNDTO grrnDTO) {
        try {
            GRRNDTO savedGRRN = grrnService.addGRRNWithItems(grrnDTO);
            ApiResponse<GRRNDTO> response = new ApiResponse<>(
                    HttpStatus.CREATED.value(),
                    "GRRN and items created successfully.",
                    savedGRRN
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            ApiResponse<GRRNDTO> errorResponse = new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Failed to create GRRN and items: " + e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // DELETE endpoint to remove GRRN and its associated items
    @DeleteMapping("/deletegrrn/{grrnId}")
    public ResponseEntity<ApiResponse<Void>> deleteGRRN(@PathVariable("grrnId") Long grrnId) {
        try {
            grrnService.deleteGRRN(grrnId);
            ApiResponse<Void> response = new ApiResponse<>(
                    HttpStatus.NO_CONTENT.value(),
                    "GRRN and items deleted successfully.",
                    null
            );
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
        } catch (Exception e) {
            ApiResponse<Void> errorResponse = new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Failed to delete GRRN and items: " + e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // GET endpoint to view GRRN data with associated GRRNItem data
    @GetMapping("/getgrrn/{grrnId}")
    public ResponseEntity<GRRNDTO> getGRRN(@PathVariable("grrnId") Long grrnId) {
        try {
            GRRNDTO grrnDTO = grrnService.getGRRNById(grrnId);
            if (grrnDTO == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Return NOT_FOUND if GRRN not found
            }
            return new ResponseEntity<>(grrnDTO, HttpStatus.OK); // Return GRRNDTO if found
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // Handle unexpected errors
        }
    }
}
