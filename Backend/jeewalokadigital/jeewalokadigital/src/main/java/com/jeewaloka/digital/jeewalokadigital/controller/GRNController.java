package com.jeewaloka.digital.jeewalokadigital.controller;

import com.jeewaloka.digital.jeewalokadigital.dto.ApiResponse;
import com.jeewaloka.digital.jeewalokadigital.dto.GRNDTO;
import com.jeewaloka.digital.jeewalokadigital.dto.GRNResponseDTO;
import com.jeewaloka.digital.jeewalokadigital.entity.GRN;
import com.jeewaloka.digital.jeewalokadigital.service.GRNService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grns")
public class GRNController {

    @Autowired
    private GRNService grnService;

    // Create a new GRN
    @PostMapping
    public ResponseEntity<ApiResponse<GRN>> createGRN(@RequestBody GRNDTO grnDTO) {
        try {
            GRN createdGRN = grnService.createGRN(grnDTO);
            ApiResponse<GRN> response = new ApiResponse<>(
                    HttpStatus.CREATED.value(),
                    "GRN created successfully.",
                    null
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            ApiResponse<GRN> errorResponse = new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Failed to create GRN: " + e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // Get a GRN by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GRNResponseDTO>> getGRN(@PathVariable Long id) {
        try {
            GRNResponseDTO grnResponse = grnService.getGRNById(id);
            ApiResponse<GRNResponseDTO> response = new ApiResponse<>(
                    HttpStatus.OK.value(),
                    "GRN fetched successfully.",
                    grnResponse
            );
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            ApiResponse<GRNResponseDTO> errorResponse = new ApiResponse<>(
                    HttpStatus.NOT_FOUND.value(),
                    "GRN not found: " + e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    // Get all GRNs
    @GetMapping
    public ResponseEntity<ApiResponse<List<GRNResponseDTO>>> getAllGRNs() {
        try {
            List<GRNResponseDTO> grns = grnService.getAllGRNs();
            ApiResponse<List<GRNResponseDTO>> response = new ApiResponse<>(
                    HttpStatus.OK.value(),
                    "All GRNs fetched successfully.",
                    grns
            );
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            ApiResponse<List<GRNResponseDTO>> errorResponse = new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Failed to fetch GRNs: " + e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // Update a GRN
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<GRN>> updateGRN(@PathVariable Long id, @RequestBody GRNDTO grnDTO) {
        try {
            GRN updatedGRN = grnService.updateGRN(id, grnDTO);
            ApiResponse<GRN> response = new ApiResponse<>(
                    HttpStatus.OK.value(),
                    "GRN updated successfully.",
                    null
            );
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            ApiResponse<GRN> errorResponse = new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Failed to update GRN: " + e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // Delete a GRN
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteGRN(@PathVariable Long id) {
        try {
            grnService.deleteGRN(id);
            ApiResponse<Void> response = new ApiResponse<>(
                    HttpStatus.NO_CONTENT.value(),
                    "GRN deleted successfully.",
                    null
            );
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
        } catch (Exception e) {
            ApiResponse<Void> errorResponse = new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Failed to delete GRN: " + e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
