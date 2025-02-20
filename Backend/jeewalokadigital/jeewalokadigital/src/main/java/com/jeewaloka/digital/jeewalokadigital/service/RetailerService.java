package com.jeewaloka.digital.jeewalokadigital.service;

import com.jeewaloka.digital.jeewalokadigital.dto.Request.RequestRetailerDTO;

import java.util.List;

public interface RetailerService {
    List<RequestRetailerDTO> getAllRetailers();
    RequestRetailerDTO getRetailerById(String RetailerId);
    void createRetailer(RequestRetailerDTO retailerDTO);
    void updateRetailer(RequestRetailerDTO retailerDTO);
    void deleteRetailer(String retailerId);
}
