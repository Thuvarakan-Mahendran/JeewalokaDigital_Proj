package com.jeewaloka.digital.jeewalokadigital.service;

import com.jeewaloka.digital.jeewalokadigital.dto.Request.SupplierRequestDTO;
import com.jeewaloka.digital.jeewalokadigital.dto.Response.SupplierResponseDTO;
import com.jeewaloka.digital.jeewalokadigital.entity.Supplier;
import com.jeewaloka.digital.jeewalokadigital.repository.SupplierRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;

    @Autowired
    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Transactional
    public Supplier saveSupplier(SupplierRequestDTO supplierRequestDTO) {
        Supplier supplier = new Supplier();

        // Generate Supplier Code if not provided
        if (supplierRequestDTO.getSupplierCode() == null || supplierRequestDTO.getSupplierCode().isEmpty()) {
            supplier.setSupplierCode(generateSupplierCode());
        } else {
            supplier.setSupplierCode(supplierRequestDTO.getSupplierCode());
        }

        supplier.setSupplierName(supplierRequestDTO.getSupplierName());
        supplier.setSupplierContact(supplierRequestDTO.getSupplierContact());
        supplier.setSupplierEmail(supplierRequestDTO.getSupplierEmail());
        supplier.setSupplierAddress(supplierRequestDTO.getSupplierAddress());
        supplier.setSupplierFax(supplierRequestDTO.getSupplierFax());
        supplier.setSupplierWebsite(supplierRequestDTO.getSupplierWebsite());
        supplier.setSupplierStatus(supplierRequestDTO.getSupplierStatus());

        return supplierRepository.save(supplier);
    }

    public List<SupplierResponseDTO> getAllSuppliers() {
        List<Supplier> suppliers = supplierRepository.findAll();

        return suppliers.stream().map(supplier ->{
            SupplierResponseDTO supplierResponseDTO = new SupplierResponseDTO();

            supplierResponseDTO.setSupplierId(supplier.getSupplierId());
            supplierResponseDTO.setSupplierCode(supplier.getSupplierCode());
            supplierResponseDTO.setSupplierName(supplier.getSupplierName());
            supplierResponseDTO.setSupplierContact(supplier.getSupplierContact());
            supplierResponseDTO.setSupplierEmail(supplier.getSupplierEmail());
            supplierResponseDTO.setSupplierAddress(supplier.getSupplierAddress());
            supplierResponseDTO.setSupplierFax(supplier.getSupplierFax());
            supplierResponseDTO.setSupplierWebsite(supplier.getSupplierWebsite());
            supplierResponseDTO.setSupplierStatus(supplier.getSupplierStatus());
            supplierResponseDTO.setSupplierCreatedDate(String.valueOf(supplier.getCreatedDate()));

            return supplierResponseDTO;
        }).collect(Collectors.toList());
    }

    public void deleteSupplier(Long id) {
        Supplier supplier = supplierRepository.findById(id).orElseThrow(() -> new RuntimeException("GRN not found"));
        supplierRepository.delete(supplier);
    }

    public Supplier updateSupplier(Long id, SupplierRequestDTO supplierRequestDTO) {
        Supplier existingSupplier = supplierRepository.findById(id).orElseThrow(() -> new RuntimeException("Supplier not found"));

        existingSupplier.setSupplierName(supplierRequestDTO.getSupplierName());
        existingSupplier.setSupplierContact(supplierRequestDTO.getSupplierContact());
        existingSupplier.setSupplierEmail(supplierRequestDTO.getSupplierEmail());
        existingSupplier.setSupplierAddress(supplierRequestDTO.getSupplierAddress());
        existingSupplier.setSupplierFax(supplierRequestDTO.getSupplierFax());
        existingSupplier.setSupplierWebsite(supplierRequestDTO.getSupplierWebsite());
        existingSupplier.setSupplierStatus(supplierRequestDTO.getSupplierStatus());

        return supplierRepository.save(existingSupplier);
    }

    public SupplierResponseDTO getSupplierById(Long id) {
       Supplier supplier = supplierRepository.findById(id).orElseThrow(() -> new RuntimeException("Supplier not found"));

       SupplierResponseDTO supplierResponseDTO = new SupplierResponseDTO();
         supplierResponseDTO.setSupplierId(supplier.getSupplierId());
            supplierResponseDTO.setSupplierCode(supplier.getSupplierCode());
            supplierResponseDTO.setSupplierName(supplier.getSupplierName());
            supplierResponseDTO.setSupplierContact(supplier.getSupplierContact());
            supplierResponseDTO.setSupplierEmail(supplier.getSupplierEmail());
            supplierResponseDTO.setSupplierAddress(supplier.getSupplierAddress());
            supplierResponseDTO.setSupplierFax(supplier.getSupplierFax());
            supplierResponseDTO.setSupplierWebsite(supplier.getSupplierWebsite());
            supplierResponseDTO.setSupplierStatus(supplier.getSupplierStatus());
            supplierResponseDTO.setSupplierCreatedDate(String.valueOf(supplier.getCreatedDate()));

            return supplierResponseDTO;
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
