package com.jeewaloka.digital.jeewalokadigital.service.impl;

import com.jeewaloka.digital.jeewalokadigital.dto.Request.RequestRetailerDTO;
import com.jeewaloka.digital.jeewalokadigital.entity.Retailer;
import com.jeewaloka.digital.jeewalokadigital.entity.bill.Bill;
import com.jeewaloka.digital.jeewalokadigital.entity.bill.BillItem;
import com.jeewaloka.digital.jeewalokadigital.repository.RetailerRepo;
import com.jeewaloka.digital.jeewalokadigital.service.RetailerService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RetailerServiceImpl implements RetailerService {

    @Autowired
    private RetailerRepo retailerRepos;

    @Autowired
    private ModelMapper modelMapper;




    @Override
    public List<RequestRetailerDTO> getAllRetailers() {
        return retailerRepos.findAll()
                .stream()
                .map(retailer -> {
                    // Map the retailer details to RequestRetailerDTO
                    RequestRetailerDTO dto = modelMapper.map(retailer, RequestRetailerDTO.class);

                    // Extract only the Bill IDs from the associated bills
                    List<Long> billIds = retailer.getBills()
                            .stream()
                            .map(Bill::getBillNO) // Assuming Bill has a getBillId() method
                            .collect(Collectors.toList());

                    // Set the bill IDs in the DTO
                    dto.setBillIds(billIds); // Assuming RequestRetailerDTO has a setBillIds() method

                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public RequestRetailerDTO getRetailerById(String retailerId) {
        // Find the Retailer entity by ID
        Retailer retailer = retailerRepos.findById(retailerId)
                .orElseThrow(() -> new RuntimeException("Retailer not found with ID: " + retailerId));

        // Map the retailer details to RequestRetailerDTO
        RequestRetailerDTO dto = modelMapper.map(retailer, RequestRetailerDTO.class);

        // Extract only the Bill IDs from the associated bills
        List<Long> billIds = retailer.getBills()
                .stream()
                .map(Bill::getBillNO) // Assuming Bill has a getBillNO() method
                .collect(Collectors.toList());

        // Set the bill IDs in the DTO
        dto.setBillIds(billIds); // Assuming RequestRetailerDTO has a setBillIds() method

        return dto;
    }



    @Override
    public void createRetailer(RequestRetailerDTO retailerDTO) {
        Retailer retailer = modelMapper.map(retailerDTO, Retailer.class);
        retailerRepos.save(retailer);
    }

    @Override
    public void updateRetailer(RequestRetailerDTO retailerDTO) {
        String retailerId = retailerDTO.getRetailerId();
        Retailer existingRetailer = retailerRepos.findById(retailerId)
                .orElseThrow(() -> new RuntimeException("Retailer not found with ID: " + retailerId));

        existingRetailer.setRetailerName(retailerDTO.getRetailerName());
        existingRetailer.setRetailerContactNo(Integer.parseInt(retailerDTO.getRetailerContactNo()));
        existingRetailer.setRetailerAddress(retailerDTO.getRetailerAddress());
        existingRetailer.setRetailerEmail(retailerDTO.getRetailerEmail());

        retailerRepos.save(existingRetailer);
    }


    @Override
    public void deleteRetailer(String retailerId) {
        // Check if retailer exists and delete it
        Retailer retailer = retailerRepos.findById(retailerId)
                .orElseThrow(() -> new RuntimeException("Retailer not found with ID: " + retailerId));
        retailerRepos.delete(retailer);
    }


}
