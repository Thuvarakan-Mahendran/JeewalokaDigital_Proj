package com.jeewaloka.digital.jeewalokadigital.controller;

import com.jeewaloka.digital.jeewalokadigital.dto.Response.UserCredResposeDTO;
import com.jeewaloka.digital.jeewalokadigital.dto.Request.UserCredentialsRequestDTO;
import com.jeewaloka.digital.jeewalokadigital.service.UserCredService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/userCreds")
@CrossOrigin
public class UserCredController {
    @Autowired
    private UserCredService userCredService;
    @PostMapping("/adduserCred")
    public ResponseEntity<String> addUserCred(@RequestBody UserCredentialsRequestDTO userCredentialsDTO){
        System.out.println("userid is " + userCredentialsDTO.getUsermark());
        userCredService.addUserCredentials(userCredentialsDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("UserCredentials saved successfully");
    }

    @GetMapping("/viewuserCred")
    public ResponseEntity<List<UserCredResposeDTO>> getUserCred(){
        List<UserCredResposeDTO> userCreds = userCredService.getUserCred();
        ResponseEntity<List<UserCredResposeDTO>> responseEntity = new ResponseEntity<>(userCreds,HttpStatus.OK);
        return responseEntity;
    }

    @DeleteMapping("/deleteUserCred/{id}")
    public ResponseEntity<String> deleteUserCred(@PathVariable Long id){
        userCredService.deleteUserCred(id);
        return ResponseEntity.status(HttpStatus.OK).body("deleted successfully");
    }

    @GetMapping("/findUserID/{username}")
    public ResponseEntity<Long> findUserID(@PathVariable String username){
//        Long userID = userCredService.findUserID(username);
        return ResponseEntity.status(HttpStatus.OK).body(userCredService.findUserID(username));
    }
}
