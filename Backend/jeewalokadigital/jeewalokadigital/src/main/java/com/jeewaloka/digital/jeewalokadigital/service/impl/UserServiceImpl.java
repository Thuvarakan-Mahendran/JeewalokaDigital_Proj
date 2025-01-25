package com.jeewaloka.digital.jeewalokadigital.service.impl;

import com.jeewaloka.digital.jeewalokadigital.dto.UserDTO;
import com.jeewaloka.digital.jeewalokadigital.entity.User;
import com.jeewaloka.digital.jeewalokadigital.repository.UserRepository;
import com.jeewaloka.digital.jeewalokadigital.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public List<UserDTO> findByRole(String role) {
        List<User> users = userRepository.findByRole(role);
        List<UserDTO> userDTOS = new ArrayList<>();
        for(User user : users){
            userDTOS.add(modelMapper.map(user,UserDTO.class));
        }
        return userDTOS;
    }

    @Override
    public List<UserDTO> searchByTerm(String searchTerm){
        List<User> users = userRepository.searchByTerm(searchTerm);
        List<UserDTO> userDTOS = new ArrayList<>();
        for(User user : users){
            userDTOS.add(modelMapper.map(user,UserDTO.class));
        }
        return userDTOS;
    }

    @Override
    public void deleteByUserID(Long userid) {
        userRepository.deleteById(userid);
    }

    @Override
    public List<UserDTO> addUsers(List<UserDTO> userDTOS) {
        List<User> userList = new ArrayList<>();
        for(UserDTO userDTO : userDTOS){
            userList.add(userRepository.save(modelMapper.map(userDTO,User.class)));
        }
        userDTOS.clear();
        for(User user : userList){
            userDTOS.add(modelMapper.map(user,UserDTO.class));
        }
        return userDTOS;
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO, Long id) {
        Optional<User> nuser = userRepository.findById(id);
        if(nuser.isPresent()){
            User puser = nuser.get();
            puser.setUname(userDTO.getUname());
            puser.setRole(userDTO.getRole());
            puser.setLastLogin(userDTO.getLastLogin());
            puser.setContact(userDTO.getContact());
            puser.setEmail(userDTO.getEmail());
            puser = userRepository.save(puser);
            userDTO = modelMapper.map(puser,UserDTO.class);
        }
        return userDTO;
    }
}
