package com.jeewaloka.digital.jeewalokadigital.controller;


import com.jeewaloka.digital.jeewalokadigital.dto.Request.RequestRetailerDTO;
import com.jeewaloka.digital.jeewalokadigital.service.RetailerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@RestController
@CrossOrigin
@RequestMapping(value = "api/retailer")
public class RetailerController {

    @Autowired
    private RetailerService retailerService;

    @GetMapping("/getAllRetailers")
    public List<RequestRetailerDTO> getAllRetailers() {return retailerService.getAllRetailers();}

    @GetMapping("/getRetailerById/{id}")
    public RequestRetailerDTO getRetailerById(String id){return retailerService.getRetailerById(id);}

    @PostMapping("/createRetailer")
    public void createRetailer(RequestRetailerDTO retailer){
        retailerService.createRetailer(retailer);
    }

    @PutMapping("/updateRetailer/{id}")
    public void updateRetailer(RequestRetailerDTO retailer){
        retailerService.updateRetailer(retailer);
    }

    @DeleteMapping("deleteRetailer/{id}")
    public void deleteRetailer(String id){retailerService.deleteRetailer(id);}









}
