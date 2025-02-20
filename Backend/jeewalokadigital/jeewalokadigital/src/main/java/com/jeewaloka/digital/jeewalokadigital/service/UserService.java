package com.jeewaloka.digital.jeewalokadigital.service;

import com.jeewaloka.digital.jeewalokadigital.dto.Request.UserResquestDTO;
import com.jeewaloka.digital.jeewalokadigital.dto.Response.UserResponseDTO;

import java.util.List;

public interface UserService{
    void deleteByUserID(Long userid);
    List<UserResponseDTO> addUsers(List<UserResquestDTO> userDTOS);
    UserResponseDTO updateUser(UserResquestDTO userDTO, Long id);
    List<UserResponseDTO> findAllUsers();
}
