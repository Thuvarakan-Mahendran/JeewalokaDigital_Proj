// package com.jeewaloka.digital.jeewalokadigital.service;
package com.jeewaloka.digital.jeewalokadigital.service;

import com.jeewaloka.digital.jeewalokadigital.dto.Request.RequestRetailerDTO;
import com.jeewaloka.digital.jeewalokadigital.dto.Response.ResponseRetailerDTO;

import java.util.List;

public interface RetailerService {
    List<ResponseRetailerDTO> getAllRetailers();

    ResponseRetailerDTO getRetailerById(String RetailerId);

    void createRetailer(RequestRetailerDTO retailerDTO);

    void updateRetailer(RequestRetailerDTO retailerDTO, String id);

    void deleteRetailer(String retailerId);

    // --- NEW METHOD SIGNATURE ---
    void updateCreditLimit(String retailerId, Float billTotal);
}