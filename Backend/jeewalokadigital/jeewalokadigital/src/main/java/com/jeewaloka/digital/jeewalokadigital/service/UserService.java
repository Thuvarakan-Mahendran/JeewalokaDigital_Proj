package com.jeewaloka.digital.jeewalokadigital.service;

import com.jeewaloka.digital.jeewalokadigital.dto.UserDTO;

import java.util.List;

public interface UserService{
    List<UserDTO> findByRole(String role);
    List<UserDTO> searchByTerm(String searchTerm);
    void deleteByUserID(Long userid);
    List<UserDTO> addUsers(List<UserDTO> userDTOS);
    UserDTO updateUser(UserDTO userDTO, Long id);
}
