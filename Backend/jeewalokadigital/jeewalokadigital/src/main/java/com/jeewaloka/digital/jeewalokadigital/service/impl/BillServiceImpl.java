
package com.jeewaloka.digital.jeewalokadigital.service.impl;

import com.jeewaloka.digital.jeewalokadigital.dto.Request.BillRequestDTO;
import com.jeewaloka.digital.jeewalokadigital.dto.Response.BillResponseDTO;
import com.jeewaloka.digital.jeewalokadigital.entity.bill.Bill;
import com.jeewaloka.digital.jeewalokadigital.mapper.BillMapper;
import com.jeewaloka.digital.jeewalokadigital.repository.BillRepository;
import com.jeewaloka.digital.jeewalokadigital.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BillServiceImpl implements BillService {
    @Autowired
    private BillRepository billRepository;
    @Autowired
    private BillMapper billMapper;

    @Override
    public BillResponseDTO addBill(BillRequestDTO billDTO) {
        Bill bill = billMapper.toBill(billDTO);
        bill = billRepository.save(bill);
        return billMapper.toBillDTO(bill);
    }

    @Override
    public List<BillResponseDTO> findAllBills() {
        return billRepository.findAll().stream()
                .map(billMapper::toBillDTO)
                .toList();
    }

    @Override
    public void deleteBill(Long id) {
        billRepository.deleteById(id);
    }
}

