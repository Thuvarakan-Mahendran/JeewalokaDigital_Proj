package com.jeewaloka.digital.jeewalokadigital.Converter;

import com.jeewaloka.digital.jeewalokadigital.dto.UserDTO;
import com.jeewaloka.digital.jeewalokadigital.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {
    public UserDTO userEntitytoDTO(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.setUID(user.getUID());
        userDTO.setUname(user.getUname());
        userDTO.setRole(user.getRole());
        userDTO.setEmail(user.getEmail());
        userDTO.setContact(user.getContact());
        userDTO.setLastLogin(user.getLastLogin());
        return userDTO;
    }
    public User userDTOtoEntity(UserDTO userDTO){
        User user = new User();
        user.setUID(userDTO.getUID());
        user.setUname(userDTO.getUname());
        user.setRole(userDTO.getRole());
        user.setEmail(userDTO.getEmail());
        user.setContact(userDTO.getContact());
        user.setLastLogin(userDTO.getLastLogin());
        return user;
    }
}
