package com.jeewaloka.digital.jeewalokadigital.mapper;

import com.jeewaloka.digital.jeewalokadigital.dto.Request.BillItemRequestDTO;
import com.jeewaloka.digital.jeewalokadigital.dto.Request.BillRequestDTO;
import com.jeewaloka.digital.jeewalokadigital.dto.Response.BillResponseDTO;
import com.jeewaloka.digital.jeewalokadigital.entity.Item;
import com.jeewaloka.digital.jeewalokadigital.entity.Retailer;
import com.jeewaloka.digital.jeewalokadigital.entity.User;
import com.jeewaloka.digital.jeewalokadigital.entity.bill.Bill;
import com.jeewaloka.digital.jeewalokadigital.entity.bill.BillItem;
import com.jeewaloka.digital.jeewalokadigital.repository.BillItemRepo;
import com.jeewaloka.digital.jeewalokadigital.repository.ItemRepository;
import com.jeewaloka.digital.jeewalokadigital.repository.RetailerRepo;
import com.jeewaloka.digital.jeewalokadigital.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class BillMapper {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RetailerRepo retailerRepo;
    @Autowired
    private BillItemRepo billItemRepo;
    @Autowired
    private ItemRepository itemRepository;
    public Bill toBill(BillRequestDTO billDTO){
        if (billDTO == null) {
            return null;
        }

        Bill bill = new Bill();
        bill.setTotal(billDTO.getTotal());
        bill.setBillCategory(billDTO.getBillCategory());

        // Fetch and set User by ID
        if (billDTO.getUserID() != null){
            User user = userRepository.findById(billDTO.getUserID())
                    .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + billDTO.getUserID()));
            bill.setUser(user);
        }

        // Fetch and set Retailer by ID
        if (billDTO.getRetailerID() != null){
            Retailer retailer = retailerRepo.findById(billDTO.getRetailerID())
                    .orElseThrow(() -> new EntityNotFoundException("Retailer not found with ID: " + billDTO.getRetailerID()));
            bill.setRetailer(retailer);
        }

        if(billDTO.getBillItems() != null){
            List<BillItemRequestDTO> billItemRequestDTOS = billDTO.getBillItems();
            List<BillItem> billItems = new ArrayList<>();
            for( BillItemRequestDTO billItemRequestDTO : billItemRequestDTOS){
                BillItem billItem = new BillItem();
                billItem.setQuantity(billItemRequestDTO.getQuantity());
                billItem.setTotalValue(billItemRequestDTO.getTotalValue());
                billItem.setBill(bill);
                Optional<Item> item = itemRepository.findById(billItemRequestDTO.getItem());
                if(item.isEmpty()){
                    throw new RuntimeException("item with " + billItemRequestDTO.getItem() + " id is not found");
                }
                billItem.setItem(item.get());
                billItems.add(billItem);
            }
//            billItemRepo.saveAll(billItems);
            bill.setBillItems(billItems);
        }

        // Fetch and set BillItem by ID
//        if (billDTO.getBillItemIDS() != null && !billDTO.getBillItemIDS().isEmpty()) {
//            List<BillItem> billItems = billDTO.getBillItemIDS().stream()
//                    .map(id -> billItemRepo.findById(id)
//                            .orElseThrow(() -> new EntityNotFoundException("BillItem not found with ID: " + id)))
//                    .collect(Collectors.toList());
//
//            // Set up bidirectional relationship
//            bill.setBillItems(billItems);
//            // Set the bill reference in each BillItem
////            billItems.forEach(item -> item.setBill(bill));
//            // After set, did we update the BillItem table with bill
//        }
        return bill;
    }
    public BillResponseDTO toBillDTO(Bill bill){
        if (bill == null) {
            return null;
        }

        BillResponseDTO billDTO = new BillResponseDTO();
        billDTO.setBillNO(bill.getBillNO());
        billDTO.setUserID(bill.getUser() != null ? bill.getUser().getUID() : null);
        billDTO.setRetailerID(bill.getRetailer() != null ? bill.getRetailer().getRetailerId() : null);
        billDTO.setTotal(bill.getTotal());
        billDTO.setDate(bill.getDate());
        billDTO.setBillCategory(bill.getBillCategory());

        // Extract IDs from BillItems if they exist
//        if (bill.getBillItems() != null && !bill.getBillItems().isEmpty()) {
//            billDTO.setBillItemIDS(bill.getBillItems().stream()
//                    .map(BillItem::getBIID)
//                    .collect(Collectors.toList()));
//        }

        return billDTO;
    }
}
