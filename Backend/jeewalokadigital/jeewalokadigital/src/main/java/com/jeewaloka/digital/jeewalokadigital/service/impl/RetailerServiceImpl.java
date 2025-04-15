package com.jeewaloka.digital.jeewalokadigital.service.impl;

import com.jeewaloka.digital.jeewalokadigital.dto.Request.RequestRetailerDTO;
import com.jeewaloka.digital.jeewalokadigital.dto.Response.ResponseRetailerDTO;
import com.jeewaloka.digital.jeewalokadigital.entity.Retailer;
import com.jeewaloka.digital.jeewalokadigital.entity.bill.Bill;
import com.jeewaloka.digital.jeewalokadigital.exception.InsufficientCreditException; //custom exception
import com.jeewaloka.digital.jeewalokadigital.repository.RetailerRepo;
import com.jeewaloka.digital.jeewalokadigital.service.RetailerService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RetailerServiceImpl implements RetailerService {

    private final RetailerRepo retailerRepo;
    private final ModelMapper modelMapper;
    private static final Float DEFAULT_CREDIT_LIMIT = 100000f;

    private ResponseRetailerDTO mapToDto(Retailer retailer) {
        ResponseRetailerDTO dto = modelMapper.map(retailer, ResponseRetailerDTO.class);
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
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ResponseRetailerDTO getRetailerById(String retailerId) {
        Retailer retailer = retailerRepo.findById(retailerId)
                .orElseThrow(() -> new RuntimeException("Retailer not found with ID: " + retailerId));
        return mapToDto(retailer);
    }

    @Override
    @Transactional // Ensure atomicity
    public void createRetailer(RequestRetailerDTO retailerDTO) {
        if (retailerDTO == null) {
            throw new IllegalArgumentException("Retailer data cannot be null");
        }
        if (retailerRepo.findByRetailerEmail(retailerDTO.getRetailerEmail()).isPresent()) {
            throw new IllegalArgumentException("Retailer with email " + retailerDTO.getRetailerEmail() + " already exists.");
        }

        Retailer retailer = Retailer.builder()
                .RetailerId(UUID.randomUUID().toString())
                .RetailerName(retailerDTO.getRetailerName())
                .RetailerAddress(retailerDTO.getRetailerAddress())
                .retailerEmail(retailerDTO.getRetailerEmail())
                .RetailerContactNo(retailerDTO.getRetailerContactNo())
                .LimitCredit(DEFAULT_CREDIT_LIMIT)
                .build();
        retailerRepo.save(retailer);
    }

    @Override
    @Transactional // Ensure atomicity
    public void updateRetailer(RequestRetailerDTO retailerDTO, String id) {
        Retailer existingRetailer = retailerRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Retailer not found with ID: " + id));
        if (retailerDTO.getRetailerEmail() != null && !retailerDTO.getRetailerEmail().equals(existingRetailer.getRetailerEmail())) {
            retailerRepo.findByRetailerEmail(retailerDTO.getRetailerEmail()).ifPresent(otherRetailer -> {
                throw new IllegalArgumentException("Email " + retailerDTO.getRetailerEmail() + " is already in use by another retailer.");
            });
        }
        existingRetailer.setRetailerName(retailerDTO.getRetailerName());
        existingRetailer.setRetailerAddress(retailerDTO.getRetailerAddress());
        existingRetailer.setRetailerEmail(retailerDTO.getRetailerEmail());
        existingRetailer.setRetailerContactNo(retailerDTO.getRetailerContactNo());
        retailerRepo.save(existingRetailer);
    }

    @Override
    @Transactional // Ensure atomicity
    public void deleteRetailer(String retailerId) {
        Retailer retailer = retailerRepo.findById(retailerId)
                .orElseThrow(() -> new RuntimeException("Retailer not found with ID: " + retailerId));
        retailerRepo.delete(retailer);
    }

    @Override
    @Transactional //make sure to roll back in case of exception
    public void updateCreditLimit(String retailerId, Float billTotal) {
        if (billTotal == null || billTotal < 0) {
            throw new IllegalArgumentException("Bill total cannot be null or negative.");
        }
        Retailer retailer = retailerRepo.findById(retailerId)
                .orElseThrow(() -> new RuntimeException("Retailer not found with ID: " + retailerId));

        Float currentLimit = retailer.getLimitCredit();
        if (currentLimit == null) {
            currentLimit = 0f;
        }

        if (billTotal > currentLimit) {
            throw new InsufficientCreditException("Insufficient credit limit for retailer " + retailerId +
                    ". Required: " + billTotal + ", Available: " + currentLimit);
        }

        retailer.setLimitCredit(currentLimit - billTotal);
        retailerRepo.save(retailer);
 }
}