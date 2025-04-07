package com.jeewaloka.digital.jeewalokadigital.controller;

import com.jeewaloka.digital.jeewalokadigital.dto.Request.UserResquestDTO;
import com.jeewaloka.digital.jeewalokadigital.dto.Response.UserResponseDTO;
import com.jeewaloka.digital.jeewalokadigital.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
//@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/getusers")
    public ResponseEntity<List<UserResponseDTO>> findAllUsers(){
        List<UserResponseDTO> userDTOS = userService.findAllUsers();
        ResponseEntity<List<UserResponseDTO>> responseEntity = new ResponseEntity<>(userDTOS, HttpStatus.OK);
        return responseEntity;
    }

    @DeleteMapping("/userdel/{userid}") //this is to delete the user from the database where the button delete can be found from the list we got through above get mappings
    public void deleteByUserID(@PathVariable Long userid){
        userService.deleteByUserID(userid);
    }

    @PostMapping("/adduser") //this is for the add user design where if there is lots of users to be added
    public ResponseEntity<UserResponseDTO> addUsers(@RequestBody UserResquestDTO userDTO){
        ResponseEntity<UserResponseDTO> responseEntity = new ResponseEntity<>(userService.addUser(userDTO), HttpStatus.CREATED);
        return responseEntity;
    }

    @PutMapping("/updateUser/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@RequestBody UserResquestDTO userDTO, @PathVariable Long id){
        ResponseEntity<UserResponseDTO> responseEntity = new ResponseEntity<>(userService.updateUser(userDTO,id),HttpStatus.OK);
        return responseEntity;
    }
}
