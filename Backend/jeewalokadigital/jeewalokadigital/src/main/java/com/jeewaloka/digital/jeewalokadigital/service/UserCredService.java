package com.jeewaloka.digital.jeewalokadigital.service;

import com.jeewaloka.digital.jeewalokadigital.dto.Request.UserCredentialsRequestDTO;
import com.jeewaloka.digital.jeewalokadigital.dto.Response.UserCredentialsResposeDTO;
import com.jeewaloka.digital.jeewalokadigital.entity.UserCredentials;

import java.util.List;

public interface UserCredService {
    UserCredentials findById(Long id);
    boolean existsByUsername(String username);
    void addUserCredentials(UserCredentialsRequestDTO userCredentialsDTO);

//    List<UserCredResposeDTO> getUserCred();

    void deleteUserCred(Long id);

    Long findUserID(String username);

    String findRole(String username);

    List<UserCredentialsResposeDTO> findAll();
}
