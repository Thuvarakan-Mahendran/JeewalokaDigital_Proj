// package com.jeewaloka.digital.jeewalokadigital.service.impl;
package com.jeewaloka.digital.jeewalokadigital.service.impl;

import com.jeewaloka.digital.jeewalokadigital.dto.Request.RequestRetailerDTO;
import com.jeewaloka.digital.jeewalokadigital.dto.Response.ResponseRetailerDTO;
import com.jeewaloka.digital.jeewalokadigital.entity.Retailer;
import com.jeewaloka.digital.jeewalokadigital.entity.bill.Bill;
import com.jeewaloka.digital.jeewalokadigital.exception.InsufficientCreditException; // <-- Import custom exception
import com.jeewaloka.digital.jeewalokadigital.repository.RetailerRepo;
import com.jeewaloka.digital.jeewalokadigital.service.RetailerService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // <-- Import Transactional

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RetailerServiceImpl implements RetailerService {

    private final RetailerRepo retailerRepo;
    private final ModelMapper modelMapper;
    private static final Float DEFAULT_CREDIT_LIMIT = 100000f; // Define default value

    // Helper method to map Retailer to ResponseRetailerDTO
    private ResponseRetailerDTO mapToDto(Retailer retailer) {
        ResponseRetailerDTO dto = modelMapper.map(retailer, ResponseRetailerDTO.class);
        // Explicitly set LimitCredit in DTO as ModelMapper might miss it if field name casing differs slightly in future
        dto.setLimitCredit(retailer.getLimitCredit());
        List<Long> billIds = retailer.getBills() != null
                ? retailer.getBills().stream().map(Bill::getBillNO).collect(Collectors.toList())
                : List.of();
        dto.setBillIds(billIds);
        return dto;
    }


    @Override
    public List<ResponseRetailerDTO> getAllRetailers() {
        return retailerRepo.findAll()
                .stream()
                .map(this::mapToDto) // Use helper method
                .collect(Collectors.toList());
    }

    @Override
    public ResponseRetailerDTO getRetailerById(String retailerId) {
        Retailer retailer = retailerRepo.findById(retailerId)
                .orElseThrow(() -> new RuntimeException("Retailer not found with ID: " + retailerId));
        return mapToDto(retailer); // Use helper method
    }

    @Override
    @Transactional // Ensure atomicity
    public void createRetailer(RequestRetailerDTO retailerDTO) {
        if (retailerDTO == null) {
            throw new IllegalArgumentException("Retailer data cannot be null");
        }
        // Optional: Check if email already exists
        if (retailerRepo.findByRetailerEmail(retailerDTO.getRetailerEmail()).isPresent()) {
            throw new IllegalArgumentException("Retailer with email " + retailerDTO.getRetailerEmail() + " already exists.");
        }

        Retailer retailer = Retailer.builder()
                .RetailerId(UUID.randomUUID().toString())
                .RetailerName(retailerDTO.getRetailerName())
                .RetailerAddress(retailerDTO.getRetailerAddress())
                .retailerEmail(retailerDTO.getRetailerEmail())
                .RetailerContactNo(retailerDTO.getRetailerContactNo())
                .LimitCredit(DEFAULT_CREDIT_LIMIT) // Explicitly set default credit limit
                // bills list is managed by JPA relationship, no need to set here
                .build();
        retailerRepo.save(retailer);
    }

    @Override
    @Transactional // Ensure atomicity
    public void updateRetailer(RequestRetailerDTO retailerDTO, String id) {
        Retailer existingRetailer = retailerRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Retailer not found with ID: " + id));

        // Optional: Check if the new email is already used by another retailer
        if (retailerDTO.getRetailerEmail() != null && !retailerDTO.getRetailerEmail().equals(existingRetailer.getRetailerEmail())) {
            retailerRepo.findByRetailerEmail(retailerDTO.getRetailerEmail()).ifPresent(otherRetailer -> {
                throw new IllegalArgumentException("Email " + retailerDTO.getRetailerEmail() + " is already in use by another retailer.");
            });
        }


        // Manually update fields from DTO to preserve LimitCredit and other fields not in DTO
        existingRetailer.setRetailerName(retailerDTO.getRetailerName());
        existingRetailer.setRetailerAddress(retailerDTO.getRetailerAddress());
        existingRetailer.setRetailerEmail(retailerDTO.getRetailerEmail());
        existingRetailer.setRetailerContactNo(retailerDTO.getRetailerContactNo());
        // DO NOT update LimitCredit from this DTO
        // modelMapper.map(retailerDTO, existingRetailer); // Avoid this as it might nullify fields not present in DTO

        retailerRepo.save(existingRetailer);
    }

    @Override
    @Transactional // Ensure atomicity
    public void deleteRetailer(String retailerId) {
        Retailer retailer = retailerRepo.findById(retailerId)
                .orElseThrow(() -> new RuntimeException("Retailer not found with ID: " + retailerId));

        // Consider business logic: Can you delete a retailer with outstanding bills/credit?
        // Add checks here if needed. Example:
        // if (retailer.getLimitCredit() < DEFAULT_CREDIT_LIMIT) { // Or check if bills list is not empty and has unpaid ones
        //    throw new RuntimeException("Cannot delete retailer with outstanding credit or bills.");
        // }

        retailerRepo.delete(retailer);
    }

    // --- NEW METHOD ---
    @Override
    @Transactional // Make sure this runs in a transaction, ideally with the Bill saving logic
    public void updateCreditLimit(String retailerId, Float billTotal) {
        if (billTotal == null || billTotal < 0) {
            throw new IllegalArgumentException("Bill total cannot be null or negative.");
        }
        Retailer retailer = retailerRepo.findById(retailerId)
                .orElseThrow(() -> new RuntimeException("Retailer not found with ID: " + retailerId));

        Float currentLimit = retailer.getLimitCredit();
        if (currentLimit == null) {
            // Handle case where limit might be null unexpectedly, maybe set to 0 or default?
            currentLimit = 0f; // Or throw an error
            // Or maybe log a warning and use the default?
            // log.warn("Retailer {} has null credit limit, defaulting to 0 for check.", retailerId);
            // currentLimit = 0f;
        }

        if (billTotal > currentLimit) {
            throw new InsufficientCreditException("Insufficient credit limit for retailer " + retailerId +
                    ". Required: " + billTotal + ", Available: " + currentLimit);
        }

        retailer.setLimitCredit(currentLimit - billTotal);
        retailerRepo.save(retailer);
        // Consider logging the credit update
        // log.info("Updated credit limit for retailer {}: Old={}, New={}", retailerId, currentLimit, retailer.getLimitCredit());
    }

    // --- NEW METHOD for Repository lookup ---
    // Add this method signature to RetailerRepo interface:
    // Optional<Retailer> findByRetailerEmail(String email);
}