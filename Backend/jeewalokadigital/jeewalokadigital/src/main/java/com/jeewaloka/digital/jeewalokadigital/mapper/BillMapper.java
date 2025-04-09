package com.jeewaloka.digital.jeewalokadigital.mapper;

import com.jeewaloka.digital.jeewalokadigital.dto.Request.BillItemRequestDTO;
import com.jeewaloka.digital.jeewalokadigital.dto.Request.BillRequestDTO;
import com.jeewaloka.digital.jeewalokadigital.dto.Response.BillResponseDTO;
import com.jeewaloka.digital.jeewalokadigital.entity.Item;
import com.jeewaloka.digital.jeewalokadigital.entity.Retailer;
import com.jeewaloka.digital.jeewalokadigital.entity.User;
import com.jeewaloka.digital.jeewalokadigital.entity.bill.Bill;
import com.jeewaloka.digital.jeewalokadigital.entity.bill.BillItem;
import com.jeewaloka.digital.jeewalokadigital.repository.BillItemRepo; // Assuming BillItemRepo might be needed if saving items separately
import com.jeewaloka.digital.jeewalokadigital.repository.ItemRepository;
import com.jeewaloka.digital.jeewalokadigital.repository.RetailerRepo;
import com.jeewaloka.digital.jeewalokadigital.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor; // <-- Use constructor injection
// import org.springframework.beans.factory.annotation.Autowired; // <-- Remove Autowired
import org.springframework.stereotype.Component;

import java.time.LocalDate; // Import if needed for date mapping (currently not used here but good practice)
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor // <-- Use constructor injection
public class BillMapper {

    // Use final fields with constructor injection
    private final UserRepository userRepository;
    private final RetailerRepo retailerRepo;
    // private final BillItemRepo billItemRepo; // Only needed if BillItems aren't cascaded from Bill
    private final ItemRepository itemRepository;

    public Bill toBill(BillRequestDTO billDTO){
        if (billDTO == null) {
            return null;
        }

        Bill bill = new Bill();
        bill.setTotal(billDTO.getTotal());
        bill.setBillCategory(billDTO.getBillCategory());
        // Bill date is usually set automatically by @CreationTimestamp or similar
        // bill.setDate(LocalDate.now()); // Or map from DTO if provided

        // Fetch and set User by ID
        if (billDTO.getUserID() != null){
            // Consider if UserID should be Long or String depending on User entity's ID type
            User user = userRepository.findById(billDTO.getUserID()) // Adjust type if User ID is String
                    .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + billDTO.getUserID()));
            bill.setUser(user);
        } else {
            throw new IllegalArgumentException("User ID cannot be null for a bill."); // Bill should belong to a user
        }

        // Fetch and set Retailer by ID (only if ID is provided)
        if (billDTO.getRetailerID() != null){
            Retailer retailer = retailerRepo.findById(billDTO.getRetailerID())
                    .orElseThrow(() -> new EntityNotFoundException("Retailer not found with ID: " + billDTO.getRetailerID()));
            bill.setRetailer(retailer); // <-- This correctly associates the Retailer entity
        } else if ("CREDIT".equalsIgnoreCase(billDTO.getBillCategory().toString())) {
            // If it's a credit bill, retailer ID *must* be provided in the DTO
            throw new IllegalArgumentException("Retailer ID cannot be null for a CREDIT bill.");
        } // If not credit, retailer can be null (e.g., direct cash sale)

        // Map BillItems
        if(billDTO.getBillItems() != null && !billDTO.getBillItems().isEmpty()){
            List<BillItem> billItems = new ArrayList<>();
            for( BillItemRequestDTO itemDTO : billDTO.getBillItems()){
                if (itemDTO.getItem() == null || itemDTO.getQuantity() == null || itemDTO.getTotalValue() == null) {
                    throw new IllegalArgumentException("Bill item details (Item ID, Quantity, Total Value) cannot be null.");
                }
                if (itemDTO.getQuantity() <= 0) {
                    throw new IllegalArgumentException("Bill item quantity must be positive.");
                }


                BillItem billItem = new BillItem();
                billItem.setQuantity(itemDTO.getQuantity());
                billItem.setTotalValue(itemDTO.getTotalValue());
                billItem.setBill(bill); // <-- Associate item with the parent bill

                // Fetch the Item entity
                Item item = itemRepository.findById(itemDTO.getItem()) // Assuming Item ID is Long
                        .orElseThrow(() -> new EntityNotFoundException("Item not found with ID: " + itemDTO.getItem()));
                billItem.setItem(item);

                // Optional: Validate if itemDTO.getTotalValue() matches item.getPrice() * itemDTO.getQuantity()
                // BigDecimal calculatedTotal = item.getUnitPrice().multiply(BigDecimal.valueOf(itemDTO.getQuantity()));
                // if (calculatedTotal.compareTo(itemDTO.getTotalValue()) != 0) { // Use BigDecimal for price
                //    log.warn("Potential mismatch for item {}: calculated total {} vs provided total {}", item.getItemCode(), calculatedTotal, itemDTO.getTotalValue());
                // }


                billItems.add(billItem);
            }
            bill.setBillItems(billItems); // <-- Set the mapped items on the bill
        } else {
            throw new IllegalArgumentException("A bill must contain at least one item."); // Bill must have items
        }

        // Ensure BillItems list is initialized even if empty (though we added validation above)
        // if (bill.getBillItems() == null) {
        //     bill.setBillItems(new ArrayList<>());
        // }

        return bill;
    }

    public BillResponseDTO toBillDTO(Bill bill){
        if (bill == null) {
            return null;
        }

        BillResponseDTO billDTO = new BillResponseDTO();
        billDTO.setBillNO(bill.getBillNO());
        billDTO.setUserID(bill.getUser() != null ? bill.getUser().getUID() : null); // Adjust based on User ID type/getter
        billDTO.setRetailerID(bill.getRetailer() != null ? bill.getRetailer().getRetailerId() : null);
        billDTO.setTotal(bill.getTotal());
        billDTO.setDate(bill.getDate()); // Assuming Bill entity has getDate() returning LocalDate/Date
        billDTO.setBillCategory(bill.getBillCategory());
        // Optionally map BillItem details back if needed in the response
        // billDTO.setBillItems(bill.getBillItems().stream().map(this::toBillItemDTO).collect(Collectors.toList()));

        return billDTO;
    }

    // Optional: Helper method if you need to map BillItem back to a DTO
    // private BillItemResponseDTO toBillItemDTO(BillItem billItem) { ... }
}