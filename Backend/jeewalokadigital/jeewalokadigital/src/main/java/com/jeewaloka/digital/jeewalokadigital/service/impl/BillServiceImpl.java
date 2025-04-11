package com.jeewaloka.digital.jeewalokadigital.service.impl;

import com.jeewaloka.digital.jeewalokadigital.dto.Request.BillRequestDTO;
import com.jeewaloka.digital.jeewalokadigital.dto.Response.BillResponseDTO;
import com.jeewaloka.digital.jeewalokadigital.entity.bill.Bill;
import com.jeewaloka.digital.jeewalokadigital.exception.InsufficientCreditException; // Make sure this import exists
import com.jeewaloka.digital.jeewalokadigital.mapper.BillMapper;
import com.jeewaloka.digital.jeewalokadigital.repository.BillRepository;
import com.jeewaloka.digital.jeewalokadigital.service.BillService;
import com.jeewaloka.digital.jeewalokadigital.service.RetailerService; // <-- Import RetailerService
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor; // <-- Import Lombok annotation
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // <-- Import Transactional

import java.util.List;

@Service
@RequiredArgsConstructor // <-- Use constructor injection via Lombok
public class BillServiceImpl implements BillService {

    // Use final fields with constructor injection
    private final BillRepository billRepository;
    private final BillMapper billMapper;
    private final RetailerService retailerService; // <-- Inject RetailerService

    @Override
    @Transactional // <-- Make the entire operation atomic
    public BillResponseDTO addBill(BillRequestDTO billDTO) {
        if (billDTO == null) {
            throw new IllegalArgumentException("Bill request data cannot be null.");
        }

        // 1. Map DTO to Bill Entity (includes fetching related entities like Retailer)
        // The mapper will throw EntityNotFoundException if User or Retailer ID is invalid.
        Bill bill = billMapper.toBill(billDTO);

        // 2. Perform Credit Check/Update *only if* category is CREDIT
        if ("CREDIT".equalsIgnoreCase(bill.getBillCategory().toString())) {
            // Ensure a retailer is actually associated for a credit bill
            if (bill.getRetailer() == null || bill.getRetailer().getRetailerId() == null) {
                // This case should ideally be caught by DTO validation, but double-check
                throw new IllegalArgumentException("Retailer ID must be provided for CREDIT bills.");
            }
            // Ensure total is valid for credit calculation
            if (bill.getTotal() == null || bill.getTotal() < 0) {
                throw new IllegalArgumentException("Bill total cannot be null or negative for credit calculation.");
            }

            // Call retailer service to check credit and update limit.
            // This method will throw InsufficientCreditException if check fails.
            // If it succeeds, the retailer's credit limit is updated within the same transaction.
            try {
                retailerService.updateCreditLimit(bill.getRetailer().getRetailerId(), bill.getTotal());
                // Optional: Log successful credit deduction
                // log.info("Credit limit updated successfully for retailer {} due to bill creation.", bill.getRetailer().getRetailerId());
            } catch (InsufficientCreditException e) {
                // Optional: Log the specific error before re-throwing or letting the GlobalExceptionHandler handle it
                // log.warn("Credit check failed for retailer {}: {}", bill.getRetailer().getRetailerId(), e.getMessage());
                throw e; // Re-throw to ensure transaction rollback and proper response
            } catch (Exception e) {
                // Catch other potential errors from updateCreditLimit (e.g., Retailer not found again, unlikely here)
                // log.error("Unexpected error during credit limit update for retailer {}: {}", bill.getRetailer().getRetailerId(), e.getMessage(), e);
                throw new RuntimeException("Failed to update credit limit for retailer " + bill.getRetailer().getRetailerId(), e);
            }

        } else if (bill.getBillCategory() == null) {
            throw new IllegalArgumentException("Bill category (e.g., CASH, CREDIT) must be specified.");
        }


        // 3. Save the Bill entity (along with cascaded BillItems)
        // This happens only if the credit check (if applicable) passed.
        Bill savedBill = billRepository.save(bill);

        // 4. Map the saved entity back to a Response DTO
        return billMapper.toBillDTO(savedBill);
    }

    @Override
    @Transactional(readOnly = true) // Good practice for read operations
    public List<BillResponseDTO> findAllBills() {
        return billRepository.findAll().stream()
                .map(billMapper::toBillDTO)
                .toList(); // Using toList() is concise
    }

    @Override
    @Transactional // Deletes should also be transactional
    public void deleteBill(Long id) {
        // Optional but recommended: Check if bill exists before trying to delete
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bill not found with ID: " + id));

        // --- IMPORTANT CONSIDERATION ---
        // Should deleting a CREDIT bill REVERSE the credit limit deduction?
        // This adds complexity. If required, you'd need to:
        // 1. Check if bill.getBillCategory() is CREDIT.
        // 2. If yes, call a *new* method in RetailerService like `increaseCreditLimit(retailerId, billTotal)`.
        // 3. Ensure this increase happens within the same transaction as the delete.
        // For now, we are just deleting the bill record.
        // if ("CREDIT".equalsIgnoreCase(bill.getBillCategory()) && bill.getRetailer() != null) {
        //     // retailerService.increaseCreditLimit(bill.getRetailer().getRetailerId(), bill.getTotal());
        // }

        billRepository.delete(bill); // Delete the fetched entity
        // Or billRepository.deleteById(id); // If you don't need the entity first
    }
}