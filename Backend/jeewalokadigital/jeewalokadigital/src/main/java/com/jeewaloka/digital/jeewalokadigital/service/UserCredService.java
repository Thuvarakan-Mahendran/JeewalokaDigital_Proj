package com.jeewaloka.digital.jeewalokadigital.service;

import com.jeewaloka.digital.jeewalokadigital.repository.UserCredRepository;
import com.jeewaloka.digital.jeewalokadigital.entity.UserCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserCredService {
    @Autowired
    private UserCredRepository userCredRepository;
    public UserCredentials findById(Long id){
        Optional<UserCredentials> user = userCredRepository.findById(id);
        UserCredentials userCredentials = user.get();
        return userCredentials;
    }
}
