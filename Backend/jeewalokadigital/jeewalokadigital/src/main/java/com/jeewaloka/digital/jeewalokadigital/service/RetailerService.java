package com.jeewaloka.digital.jeewalokadigital.service;

import com.jeewaloka.digital.jeewalokadigital.dto.Request.RequestRetailerDTO;

import java.util.List;

public interface RetailerService {
    List<RequestRetailerDTO> getAllRetailers();
    RequestRetailerDTO getRetailerById(Integer RetailerId);
    void createRetailer(RequestRetailerDTO retailerDTO);
    void updateRetailer(RequestRetailerDTO retailerDTO);
    void deleteRetailer(Integer retailerId);
}
