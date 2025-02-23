package com.jeewaloka.digital.jeewalokadigital.service.impl;

import com.jeewaloka.digital.jeewalokadigital.dto.Request.RequestRetailerDTO;
import com.jeewaloka.digital.jeewalokadigital.entity.Retailer;
import com.jeewaloka.digital.jeewalokadigital.entity.bill.Bill;
import com.jeewaloka.digital.jeewalokadigital.repository.RetailerRepo;
import com.jeewaloka.digital.jeewalokadigital.service.RetailerService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RetailerServiceImpl implements RetailerService {

    private final RetailerRepo retailerRepo;
    private final ModelMapper modelMapper;

    @Override
    public List<RequestRetailerDTO> getAllRetailers() {
        return retailerRepo.findAll()
                .stream()
                .map(retailer -> {
                    RequestRetailerDTO dto = modelMapper.map(retailer, RequestRetailerDTO.class);

                    List<Long> billIds = retailer.getBills() != null
                            ? retailer.getBills().stream().map(Bill::getBillNO).collect(Collectors.toList())
                            : List.of();

                    dto.setBillIds(billIds);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public RequestRetailerDTO getRetailerById(Integer retailerId) {
        Retailer retailer = retailerRepo.findById(retailerId)
                .orElseThrow(() -> new RuntimeException("Retailer not found with ID: " + retailerId));

        RequestRetailerDTO dto = modelMapper.map(retailer, RequestRetailerDTO.class);

        List<Long> billIds = retailer.getBills() != null
                ? retailer.getBills().stream().map(Bill::getBillNO).collect(Collectors.toList())
                : List.of();

        dto.setBillIds(billIds);
        return dto;
    }

    @Override
    public void createRetailer(RequestRetailerDTO retailerDTO) {
        if (retailerDTO == null) {
            throw new IllegalArgumentException("Retailer data cannot be null");
        }

        Retailer retailer = modelMapper.map(retailerDTO, Retailer.class);
        retailerRepo.save(retailer);
    }

    @Override
    public void updateRetailer(RequestRetailerDTO retailerDTO) {
        if (retailerDTO == null || retailerDTO.getRetailerId() == null) {
            throw new IllegalArgumentException("Retailer ID cannot be null for update");
        }

        Retailer existingRetailer = retailerRepo.findById(retailerDTO.getRetailerId())
                .orElseThrow(() -> new RuntimeException("Retailer not found with ID: " + retailerDTO.getRetailerId()));

        modelMapper.map(retailerDTO, existingRetailer);
        retailerRepo.save(existingRetailer);
    }

    @Override
    public void deleteRetailer(Integer retailerId) {
        if (retailerId == null) {
            throw new IllegalArgumentException("Retailer ID cannot be null for deletion");
        }

        Retailer retailer = retailerRepo.findById(retailerId)
                .orElseThrow(() -> new RuntimeException("Retailer not found with ID: " + retailerId));

        retailerRepo.delete(retailer);
    }
}
