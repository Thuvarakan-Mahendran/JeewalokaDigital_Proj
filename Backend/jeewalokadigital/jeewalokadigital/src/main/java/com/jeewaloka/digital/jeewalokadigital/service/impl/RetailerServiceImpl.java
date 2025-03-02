package com.jeewaloka.digital.jeewalokadigital.service.impl;

import com.jeewaloka.digital.jeewalokadigital.dto.Request.RequestRetailerDTO;
import com.jeewaloka.digital.jeewalokadigital.dto.Response.ResponseRetailerDTO;
import com.jeewaloka.digital.jeewalokadigital.entity.Retailer;
import com.jeewaloka.digital.jeewalokadigital.entity.bill.Bill;
import com.jeewaloka.digital.jeewalokadigital.repository.RetailerRepo;
import com.jeewaloka.digital.jeewalokadigital.service.RetailerService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RetailerServiceImpl implements RetailerService {

    private final RetailerRepo retailerRepo;
    private final ModelMapper modelMapper;

    @Override
    public List<ResponseRetailerDTO> getAllRetailers() {
        return retailerRepo.findAll()
                .stream()
                .map(retailer -> {
                    ResponseRetailerDTO dto = modelMapper.map(retailer, ResponseRetailerDTO.class);

                    List<Long> billIds = retailer.getBills() != null
                            ? retailer.getBills().stream().map(Bill::getBillNO).collect(Collectors.toList())
                            : List.of();

                    dto.setBillIds(billIds);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public ResponseRetailerDTO getRetailerById(String retailerId) {
        Retailer retailer = retailerRepo.findById(retailerId)
                .orElseThrow(() -> new RuntimeException("Retailer not found with ID: " + retailerId));

        ResponseRetailerDTO dto = modelMapper.map(retailer, ResponseRetailerDTO.class);

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

        Retailer retailer = Retailer.builder()
                .RetailerId(UUID.randomUUID().toString())
                .RetailerName(retailerDTO.getRetailerName())
                .RetailerAddress(retailerDTO.getRetailerAddress())
                .RetailerEmail(retailerDTO.getRetailerEmail())
                .RetailerContactNo(retailerDTO.getRetailerContactNo())
                .build();
        retailerRepo.save(retailer);
    }

    @Override
    public void updateRetailer(RequestRetailerDTO retailerDTO, String id) {

        Retailer existingRetailer = retailerRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Retailer not found with ID: " + id));

        modelMapper.map(retailerDTO, existingRetailer);
        retailerRepo.save(existingRetailer);
    }

    @Override
    public void deleteRetailer(String retailerId) {

        Retailer retailer = retailerRepo.findById(retailerId)
                .orElseThrow(() -> new RuntimeException("Retailer not found with ID: " + retailerId));

        retailerRepo.delete(retailer);
    }
}
