package com.jeewaloka.digital.jeewalokadigital.controller;

import com.jeewaloka.digital.jeewalokadigital.util.ApiResponse;
import com.jeewaloka.digital.jeewalokadigital.dto.Request.GRNRequestDTO;
import com.jeewaloka.digital.jeewalokadigital.dto.Response.GRNResponseDTO;
import com.jeewaloka.digital.jeewalokadigital.entity.GRN;
import com.jeewaloka.digital.jeewalokadigital.service.GRNService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api/grns")
@CrossOrigin

public class GRNController {

    @Autowired
    private GRNService grnService;

    // Create a new GRN


    @PostMapping("/creategrn")
    public ResponseEntity<ApiResponse<GRN>> createGRN(@RequestBody GRNRequestDTO grnDTO) {
        try {
            // Decode Base64 string(s) into byte[]
            List<byte[]> decodedAttachments = new ArrayList<>();
            if (grnDTO.getGrnAttachment() != null) {
                for (String base64File : grnDTO.getGrnAttachment()) {
                    byte[] fileBytes = Base64.getDecoder().decode(base64File);
                    decodedAttachments.add(fileBytes);
                }
            }

            // Convert GRNRequestDTO to GRN Entity (assuming GRN entity supports binary storage)
            GRN createdGRN = grnService.createGRN(grnDTO, decodedAttachments);

            ApiResponse<GRN> response = new ApiResponse<>(
                    HttpStatus.CREATED.value(),
                    "GRN created successfully.",
                    null
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            ApiResponse<GRN> errorResponse = new ApiResponse<>(
                    HttpStatus.BAD_REQUEST.value(),
                    "Invalid base64 file format: " + e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
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
    @GetMapping("getgrn/{id}")
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
    @GetMapping("/getallgrns")
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
    @PutMapping("editgrn/{id}")
    public ResponseEntity<ApiResponse<GRN>> updateGRN(@PathVariable Long id, @RequestBody GRNRequestDTO grnDTO) {
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
    @DeleteMapping("deletegrn/{id}")
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
