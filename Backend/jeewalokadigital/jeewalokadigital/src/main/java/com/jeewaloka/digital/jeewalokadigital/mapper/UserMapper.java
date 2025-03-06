package com.jeewaloka.digital.jeewalokadigital.mapper;

import com.jeewaloka.digital.jeewalokadigital.dto.Request.UserResquestDTO;
import com.jeewaloka.digital.jeewalokadigital.dto.Response.UserResponseDTO;
import com.jeewaloka.digital.jeewalokadigital.entity.User;
import com.jeewaloka.digital.jeewalokadigital.entity.bill.Bill;
import com.jeewaloka.digital.jeewalokadigital.repository.BillRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {
    @Autowired
    private BillRepository billRepository;

    public User toUser(UserResquestDTO userDTO){
        if (userDTO == null) {
            return null;
        }
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setContact(userDTO.getContact());
//        user.setLastLogin(userDTO.getLastLogin());
        user.setRole(userDTO.getRole());
        user.setUname(userDTO.getUname());
//        if (userDTO.getBillIDS() != null && !userDTO.getBillIDS().isEmpty()) {
//            List<Bill> bills = userDTO.getBillIDS().stream()
//                    .map(id -> billRepository.findById(id)
//                            .orElseThrow(() -> new EntityNotFoundException("user not found with ID: " + id)))
//                    .toList();
//
//            // Set up bidirectional relationship
//            user.setBills(bills);
//            // Set the user reference in each bills
//            bills.forEach(item -> item.setUser(user));
//        }
        return user;
    }

    public UserResponseDTO toDTO(User user){
        if (user == null) {
            return null;
        }
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setUID(user.getUID());
        userResponseDTO.setRole(user.getRole());
        userResponseDTO.setEmail(user.getEmail());
        userResponseDTO.setContact(user.getContact());
        userResponseDTO.setLastLogin(user.getLastLogin());
        userResponseDTO.setUname(user.getUname());
//        if(user.getBills() != null) {
//            userResponseDTO.setBillIDS(user.getBills().stream()
//                    .map(Bill::getBillNO)
//                    .toList());
//        }
        return userResponseDTO;
    }
}
