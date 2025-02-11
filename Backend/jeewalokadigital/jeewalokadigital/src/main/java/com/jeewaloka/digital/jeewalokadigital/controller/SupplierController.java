package com.jeewaloka.digital.jeewalokadigital.controller;

import com.jeewaloka.digital.jeewalokadigital.dto.SupplierDTO;
import com.jeewaloka.digital.jeewalokadigital.entity.Supplier;
import com.jeewaloka.digital.jeewalokadigital.service.SupplierService;
import com.jeewaloka.digital.jeewalokadigital.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/suppliers")
@CrossOrigin
public class SupplierController {

    @Autowired
    private  SupplierService supplierService;


    @PostMapping("/createsupplier")
    public ResponseEntity<ApiResponse<Supplier>> createSupplier(@RequestBody SupplierDTO supplierDTO) {
        try {
            Supplier createdSupplier = supplierService.saveSupplier(supplierDTO);
            ApiResponse<Supplier> response = new ApiResponse<>(
                    HttpStatus.CREATED.value(),
                    "Supplier created successfully.",
                    createdSupplier
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            ApiResponse<Supplier> errorResponse = new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Failed to create Supplier: " + e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
