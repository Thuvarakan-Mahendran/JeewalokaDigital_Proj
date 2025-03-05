package com.jeewaloka.digital.jeewalokadigital.controller;

import com.jeewaloka.digital.jeewalokadigital.dto.UserCredentialsDTO;
import com.jeewaloka.digital.jeewalokadigital.service.UserCredService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/userCreds")
public class UserCredController {
    @Autowired
    private UserCredService userCredService;
    @PostMapping("/adduserCred")
    public ResponseEntity<String> addUserCred(@RequestBody UserCredentialsDTO userCredentialsDTO){
        userCredService.saveUserCred(userCredentialsDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("UserCredentials saved successfully");
    }
}
