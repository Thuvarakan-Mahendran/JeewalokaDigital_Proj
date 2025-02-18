
package com.jeewaloka.digital.jeewalokadigital.service.impl;

import com.jeewaloka.digital.jeewalokadigital.dto.BillDTO;
import com.jeewaloka.digital.jeewalokadigital.entity.bill.Bill;
import com.jeewaloka.digital.jeewalokadigital.mapper.BillMapper;
import com.jeewaloka.digital.jeewalokadigital.repository.BillRepository;
import com.jeewaloka.digital.jeewalokadigital.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BillServiceImpl implements BillService {
    @Autowired
    private BillRepository billRepository;
    @Autowired
    private BillMapper billMapper;
    @Override
    public List<BillDTO> findByDate(LocalDateTime issueDate) {
        List<Bill> bills = billRepository.findByDate(issueDate);
//        List<BillDTO> BillDTOS = new ArrayList<>();
//        for(Bill bill : bills){
//            BillDTOS.add(billMapper.toBillDTO(bill));
//        }
//        return BillDTOS;
        return bills.stream()
                .map((billDTO) -> billMapper.toBillDTO(billDTO))
                .toList();
    }

    @Override
    public List<BillDTO> searchByTerm(String searchTerm) {
        List<Bill> bills = billRepository.searchByTerm(searchTerm);
//        List<BillDTO> BillDTOS = new ArrayList<>();
//        for(Bill bill : bills){
//            BillDTOS.add(billMapper.toBillDTO(bill));
//        }
//        return BillDTOS;
        return bills.stream()
                .map((billDTO) -> billMapper.toBillDTO(billDTO))
                .toList();
    }

    @Override
    public BillDTO addBill(BillDTO billDTO) {
        Bill bill = billMapper.toBill(billDTO);
        bill = billRepository.save(bill);
        return billMapper.toBillDTO(bill);
    }
}

