package com.jeewaloka.digital.jeewalokadigital.service;

import com.jeewaloka.digital.jeewalokadigital.dto.SupplierDTO;
import com.jeewaloka.digital.jeewalokadigital.entity.Supplier;
import com.jeewaloka.digital.jeewalokadigital.repository.SupplierRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;

    @Autowired
    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Transactional
    public Supplier saveSupplier(SupplierDTO supplierDTO) {
        Supplier supplier = new Supplier();

        // Generate Supplier Code if not provided
        if (supplierDTO.getSupplierCode() == null || supplierDTO.getSupplierCode().isEmpty()) {
            supplier.setSupplierCode(generateSupplierCode());
        } else {
            supplier.setSupplierCode(supplierDTO.getSupplierCode());
        }

        supplier.setSupplierName(supplierDTO.getSupplierName());
        supplier.setSupplierContact(supplierDTO.getSupplierContact());
        supplier.setSupplierEmail(supplierDTO.getSupplierEmail());
        supplier.setSupplierAddress(supplierDTO.getSupplierAddress());
        supplier.setSupplierFax(supplierDTO.getSupplierFax());
        supplier.setSupplierWebsite(supplierDTO.getSupplierWebsite());
        supplier.setSupplierStatus(supplierDTO.getSupplierStatus());

        return supplierRepository.save(supplier);
    }

    private String generateSupplierCode() {
        String lastCode = supplierRepository.findLastSupplierCode();

        int newNumber = 1; // Default for first supplier

        if (lastCode != null) {
            try {
                // Extract number from "SUP-001"
                newNumber = Integer.parseInt(lastCode.replace("SUP-", "")) + 1;
            } catch (NumberFormatException e) {
                newNumber = 1; // If error, start from 1
            }
        }

        return String.format("SUP-%03d", newNumber); // Format to "SUP-001"
    }
}
