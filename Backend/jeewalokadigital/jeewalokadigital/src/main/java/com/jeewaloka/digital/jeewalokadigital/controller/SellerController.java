package com.jeewaloka.digital.jeewalokadigital.controller;

import com.jeewaloka.digital.jeewalokadigital.dto.SellerDTO;
import com.jeewaloka.digital.jeewalokadigital.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "api/v1")
public class SellerController {

    @Autowired
    private SellerService sellerService;

    @GetMapping("/getsellers")
    public List<SellerDTO> getSellers() {
        return sellerService.getAllSelles();
    }

    @PostMapping("/saveseller")
    public SellerDTO saveSeller(@RequestBody SellerDTO sellerDTO) {
        return sellerService.saveSeller(sellerDTO);
    }

    @PutMapping("/editseller")
    public SellerDTO editSeller(@RequestBody SellerDTO sellerDTO){
        return sellerService.editSeller(sellerDTO);
    }
    @DeleteMapping("/deleteseller")
    public String deleteUser(@RequestBody SellerDTO sellerDTO){
        return sellerService.deleteSeller(sellerDTO);
    }

}
