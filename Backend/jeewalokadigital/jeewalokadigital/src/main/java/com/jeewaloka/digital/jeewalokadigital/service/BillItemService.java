package com.jeewaloka.digital.jeewalokadigital.service;

import com.jeewaloka.digital.jeewalokadigital.dto.Request.BillItemRequestDTO;
import com.jeewaloka.digital.jeewalokadigital.dto.Request.BillRequestDTO;
import com.jeewaloka.digital.jeewalokadigital.dto.Response.BillItemResponseDTO;

import java.util.List;

public interface BillItemService {
    BillItemResponseDTO addBillItem(BillItemRequestDTO billDTO);

    void deleteBillItem(Long id);

    void addBillItems(List<BillItemRequestDTO> billItemRequestDTOList);
}
