package com.jeewaloka.digital.jeewalokadigital.controller;

import com.jeewaloka.digital.jeewalokadigital.dto.Request.RequestRetailerDTO;
import com.jeewaloka.digital.jeewalokadigital.dto.Response.ResponseRetailerDTO;
import com.jeewaloka.digital.jeewalokadigital.entity.Retailer;
import com.jeewaloka.digital.jeewalokadigital.service.RetailerService;
import com.jeewaloka.digital.jeewalokadigital.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/retailer")
@RequiredArgsConstructor
public class RetailerController {
    private final RetailerService retailerService;

    @GetMapping("/getAllRetailers")
    public ResponseEntity<ApiResponse<List<ResponseRetailerDTO>>> getAllRetailers() {
        return new ResponseEntity<>(
                new ApiResponse<>(
                        200,
                        "retailers details",
                        retailerService.getAllRetailers()
                ),
                HttpStatus.OK
        );
    }

    @GetMapping("/getRetailerById/{id}")
    public ResponseEntity<ApiResponse<ResponseRetailerDTO>> getRetailerById(@PathVariable String id) {
        return new ResponseEntity<>(
                new ApiResponse<>(
                        200,
                        "retailer details found",
                        retailerService.getRetailerById(id)
                ),
                HttpStatus.OK
        );
    }

    @PostMapping("/createRetailer")
    public ResponseEntity<ApiResponse<Retailer>> createRetailer(@RequestBody RequestRetailerDTO retailer) {
        retailerService.createRetailer(retailer);
        System.out.println(retailer.getRetailerName());
        return new ResponseEntity<>(
                new ApiResponse<>(
                        201,
                        "retailer created",
                        null
                ),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/updateRetailer/{id}")
    public ResponseEntity<ApiResponse<Retailer>> updateRetailer(@RequestBody RequestRetailerDTO retailer, @PathVariable String id) {
        retailerService.updateRetailer(retailer, id);
        return new ResponseEntity<>(
                new ApiResponse<>(
                        201,
                        "retailer updated",
                        null
                ),
                HttpStatus.CREATED
        );
    }

    @DeleteMapping("/deleteRetailer/{id}")
    public ResponseEntity<ApiResponse<Retailer>> deleteRetailer(@PathVariable String id) {
        retailerService.deleteRetailer(id);
        return new ResponseEntity<>(
                new ApiResponse<>(
                        204,
                        "retailer deleted",
                        null
                ),
                HttpStatus.NO_CONTENT
        );
    }
}
