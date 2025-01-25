package com.jeewaloka.digital.jeewalokadigital.controller;

import com.jeewaloka.digital.jeewalokadigital.Converter.UserConverter;
import com.jeewaloka.digital.jeewalokadigital.dto.UserDTO;
import com.jeewaloka.digital.jeewalokadigital.entity.User;
import com.jeewaloka.digital.jeewalokadigital.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserConverter userConverter;
    @GetMapping("/role/{role}")
    public ResponseEntity<List<UserDTO>> findByRole(@PathVariable String role) {
        List<User> users = userService.findByRole(role);
        List<UserDTO> userDTOS = new ArrayList<>();
        for(User user : users){
            UserDTO userDTO = userConverter.userEntitytoDTO(user);
            userDTOS.add(userDTO);
        }
        ResponseEntity<List<UserDTO>> responseEntity = new ResponseEntity<>(userDTOS, HttpStatus.OK);
        return responseEntity;
    }
    @GetMapping("/searcher")
    public ResponseEntity<List<UserDTO>> findBySearchTerm(@RequestParam("searchTerm") String searchTerm){
        List<User> users = userService.searchByTerm(searchTerm);
        List<UserDTO> userDTOS = new ArrayList<>();
        for(User user : users){
            UserDTO userDTO = userConverter.userEntitytoDTO(user);
            userDTOS.add(userDTO);
        }
        ResponseEntity<List<UserDTO>> responseEntity = new ResponseEntity<>(userDTOS, HttpStatus.OK);
        return responseEntity;
    }
    @DeleteMapping("/{userid}") //this is to delete the user from the database where the button delete can be found from the list we got through above get mappings
    public void deleteByUserID(@PathVariable Long userid){
        userService.deleteByUserID(userid);
    }

    @PostMapping("/adduser-list") //this is for the add user design where if there is lots of users to be added
    public void addUsers(@RequestBody List<UserDTO> userDTOS){
        List<User> users = new ArrayList<>();
        for(UserDTO userDTO : userDTOS){
            users.add(userConverter.userDTOtoEntity(userDTO));
        }
        userService.addUsers(users);
    }
}
