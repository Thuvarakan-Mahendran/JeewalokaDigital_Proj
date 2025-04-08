package com.jeewaloka.digital.jeewalokadigital.controller;

import com.jeewaloka.digital.jeewalokadigital.dto.Request.SupplierRequestDTO;
import com.jeewaloka.digital.jeewalokadigital.dto.Response.SupplierResponseDTO;
import com.jeewaloka.digital.jeewalokadigital.entity.Supplier;
import com.jeewaloka.digital.jeewalokadigital.service.SupplierService;
import com.jeewaloka.digital.jeewalokadigital.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
//@CrossOrigin
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    @PostMapping("/savesupplier")
    public ResponseEntity<ApiResponse<Supplier>> createSupplier(@RequestBody SupplierRequestDTO supplierDTO) {
        try {
            System.out.println(supplierDTO.getSupplierName());
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

    @GetMapping("/getallsuppliers")
    public ResponseEntity<ApiResponse<List<SupplierResponseDTO>>> getAllSuppliers() {
        try {
            List<SupplierResponseDTO> suppliers = supplierService.getAllSuppliers();
            ApiResponse<List<SupplierResponseDTO>> response = new ApiResponse<>(
                    HttpStatus.OK.value(),
                    "Suppliers retrieved successfully.",
                    suppliers
            );
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            ApiResponse<List<SupplierResponseDTO>> errorResponse = new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Failed to retrieve suppliers: " + e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    @DeleteMapping("deletesupplier/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSupplier(@PathVariable Long id) {
        try {
            supplierService.deleteSupplier(id);
            ApiResponse<Void> response = new ApiResponse<>(
                    HttpStatus.NO_CONTENT.value(),
                    "Supplier deleted successfully.",
                    null
            );
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
        } catch (Exception e) {
            ApiResponse<Void> errorResponse = new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Failed to delete Supplier: " + e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PutMapping("editsupplier/{id}")
    public ResponseEntity<ApiResponse<Supplier>> updateSupplier(@PathVariable Long id, @RequestBody SupplierRequestDTO supplierDTO) {
        try {
            Supplier updatedSupplier = supplierService.updateSupplier(id, supplierDTO);
            ApiResponse<Supplier> response = new ApiResponse<>(
                    HttpStatus.OK.value(),
                    "Supplier updated successfully.",
                    updatedSupplier
            );
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            ApiResponse<Supplier> errorResponse = new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Failed to update Supplier: " + e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    @GetMapping("/getsupplier/{id}")
    public ResponseEntity<ApiResponse<SupplierResponseDTO>> getSupplierById(@PathVariable Long id) {
        try {
            SupplierResponseDTO supplier = supplierService.getSupplierById(id);
            ApiResponse<SupplierResponseDTO> response = new ApiResponse<>(
                    HttpStatus.OK.value(),
                    "Supplier retrieved successfully.",
                    supplier
            );
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            ApiResponse<SupplierResponseDTO> errorResponse = new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Failed to retrieve supplier: " + e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
