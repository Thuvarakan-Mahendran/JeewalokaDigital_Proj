package com.jeewaloka.digital.jeewalokadigital.controller;

import com.jeewaloka.digital.jeewalokadigital.dto.UserDTO;
import com.jeewaloka.digital.jeewalokadigital.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/role/{role}")
    public ResponseEntity<List<UserDTO>> findByRole(@PathVariable String role) {
        List<UserDTO> userDTOS = userService.findByRole(role);
        ResponseEntity<List<UserDTO>> responseEntity = new ResponseEntity<>(userDTOS, HttpStatus.OK);
        return responseEntity;
    }
    @GetMapping("/searcher")
    public ResponseEntity<List<UserDTO>> findBySearchTerm(@RequestParam("searchTerm") String searchTerm){
        List<UserDTO> userDTOS = userService.searchByTerm(searchTerm);
        ResponseEntity<List<UserDTO>> responseEntity = new ResponseEntity<>(userDTOS, HttpStatus.OK);
        return responseEntity;
    }
    @DeleteMapping("/{userid}") //this is to delete the user from the database where the button delete can be found from the list we got through above get mappings
    public void deleteByUserID(@PathVariable Long userid){
        userService.deleteByUserID(userid);
    }

    @PostMapping("/adduser-list") //this is for the add user design where if there is lots of users to be added
    public ResponseEntity<List<UserDTO>> addUsers(@RequestBody List<UserDTO> userDTOS){
        ResponseEntity<List<UserDTO>> responseEntity = new ResponseEntity<>(userService.addUsers(userDTOS), HttpStatus.CREATED);
        return responseEntity;
    }

    @PutMapping("/updateUser/{id}")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO, @PathVariable Long id){
        ResponseEntity<UserDTO> responseEntity = new ResponseEntity<>(userService.updateUser(userDTO,id),HttpStatus.OK);
        return responseEntity;
    }
}
