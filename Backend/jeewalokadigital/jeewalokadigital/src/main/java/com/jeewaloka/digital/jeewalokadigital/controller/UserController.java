package com.jeewaloka.digital.jeewalokadigital.controller;

import com.jeewaloka.digital.jeewalokadigital.Converter.UserConverter;
import com.jeewaloka.digital.jeewalokadigital.dto.UserDTO;
import com.jeewaloka.digital.jeewalokadigital.entity.User;
import com.jeewaloka.digital.jeewalokadigital.repository.UserRepository;
import com.jeewaloka.digital.jeewalokadigital.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserConverter userConverter;
    @GetMapping("/{role}")
    public ResponseEntity<UserDTO> findByRole(@PathVariable String role) {
        User user = userService.findByRole(role);
        UserDTO userDTO = userConverter.userEntitytoDTO(user);
        ResponseEntity<UserDTO> responseEntity = new ResponseEntity<>(userDTO, HttpStatus.OK);
        return responseEntity;
    }
}
