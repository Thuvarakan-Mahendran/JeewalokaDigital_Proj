
package com.jeewaloka.digital.jeewalokadigital.service.impl;

import com.jeewaloka.digital.jeewalokadigital.dto.BillDTO;
import com.jeewaloka.digital.jeewalokadigital.entity.bill.Bill;
import com.jeewaloka.digital.jeewalokadigital.repository.BillRepository;
import com.jeewaloka.digital.jeewalokadigital.service.BillService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BillServiceImpl implements BillService {
    @Autowired
    private BillRepository billRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public List<BillDTO> findByDate(LocalDateTime issueDate) {
        List<Bill> bills = billRepository.findByDate(issueDate);
        List<BillDTO> BillDTOS = new ArrayList<>();
        for(Bill bill : bills){
            BillDTOS.add(modelMapper.map(bill,BillDTO.class));
        }
        return BillDTOS;
    }

    @Override
    public List<BillDTO> searchByTerm(String searchTerm) {
        List<Bill> bills = billRepository.searchByTerm(searchTerm);
        List<BillDTO> BillDTOS = new ArrayList<>();
        for(Bill bill : bills){
            BillDTOS.add(modelMapper.map(bill,BillDTO.class));
        }
        return BillDTOS;
    }
}

