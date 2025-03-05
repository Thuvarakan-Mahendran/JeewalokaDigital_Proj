package com.jeewaloka.digital.jeewalokadigital.service;

import com.jeewaloka.digital.jeewalokadigital.dto.UserCredentialsDTO;
import com.jeewaloka.digital.jeewalokadigital.entity.UserCredentials;

public interface UserCredService {
    UserCredentials findById(Long id);
    boolean existsByUsername(String username);
    void saveUserCred(UserCredentialsDTO userCredentialsDTO);
}
