package com.jeewaloka.digital.jeewalokadigital.service;

import com.jeewaloka.digital.jeewalokadigital.dto.SellerDTO;
import com.jeewaloka.digital.jeewalokadigital.model.Seller;
import com.jeewaloka.digital.jeewalokadigital.repository.SellerRepo;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class SellerService {

    @Autowired
    private SellerRepo sellerRepo;

    @Autowired
    private ModelMapper modelMapper;

    public List<SellerDTO> getAllSelles() {
        List<Seller> sellerList = sellerRepo.findAll();
        return modelMapper.map(sellerList, new TypeToken<List<SellerDTO>>() {}.getType());
    }

    public SellerDTO saveSeller(SellerDTO sellerDTO) {
        sellerRepo.save(modelMapper.map(sellerDTO, Seller.class));
        return sellerDTO;
    }

    public SellerDTO editSeller(SellerDTO sellerDTO) {
        sellerRepo.save(modelMapper.map(sellerDTO, Seller.class));
        return sellerDTO;
    }

    public String deleteSeller(SellerDTO sellerDTO) {
        sellerRepo.delete(modelMapper.map(sellerDTO, Seller.class));
        return "Seller deleted";
    }
}
