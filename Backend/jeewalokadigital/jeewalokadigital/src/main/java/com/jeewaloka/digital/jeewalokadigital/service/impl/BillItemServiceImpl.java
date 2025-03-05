package com.jeewaloka.digital.jeewalokadigital.service.impl;

import com.jeewaloka.digital.jeewalokadigital.dto.Request.BillItemRequestDTO;
import com.jeewaloka.digital.jeewalokadigital.dto.Response.BillItemResponseDTO;
import com.jeewaloka.digital.jeewalokadigital.entity.Item;
import com.jeewaloka.digital.jeewalokadigital.entity.bill.Bill;
import com.jeewaloka.digital.jeewalokadigital.entity.bill.BillItem;
import com.jeewaloka.digital.jeewalokadigital.mapper.BillItemMapper;
import com.jeewaloka.digital.jeewalokadigital.repository.BillItemRepo;
import com.jeewaloka.digital.jeewalokadigital.repository.BillRepository;
import com.jeewaloka.digital.jeewalokadigital.repository.ItemRepository;
import com.jeewaloka.digital.jeewalokadigital.service.BillItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BillItemServiceImpl implements BillItemService {
    @Autowired
    private BillItemMapper billItemMapper;
    @Autowired
    private BillItemRepo billItemRepo;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private BillRepository billRepository;
    @Override
    public BillItemResponseDTO addBillItem(BillItemRequestDTO billItemDTO) {
        BillItem bill = billItemMapper.toBillItem(billItemDTO);
        bill = billItemRepo.save(bill);
        return billItemMapper.toBillItemDTO(bill);
    }

    @Override
    public void deleteBillItem(Long id) {
        billItemRepo.deleteById(id);
    }

    @Override
    public void addBillItems(List<BillItemRequestDTO> billItemRequestDTOList) {
        for(BillItemRequestDTO request : billItemRequestDTOList){
            BillItem billItem = new BillItem();
            billItem.setQuantity(request.getQuantity());
            billItem.setTotalValue(request.getTotalValue());
            Optional<Item> item = itemRepository.findById(request.getItem());
            item.ifPresent(billItem::setItem);
//            Optional<Bill> bill = billRepository.findById(request.getBill());
//            bill.ifPresent(billItem::setBill);
            billItemRepo.save(billItem);
        }
//        return null;
    }
}
