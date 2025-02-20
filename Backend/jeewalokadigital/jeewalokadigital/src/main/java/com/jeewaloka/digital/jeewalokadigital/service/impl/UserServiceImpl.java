package com.jeewaloka.digital.jeewalokadigital.service.impl;

import com.jeewaloka.digital.jeewalokadigital.dto.Request.UserResquestDTO;
import com.jeewaloka.digital.jeewalokadigital.dto.Response.UserResponseDTO;
import com.jeewaloka.digital.jeewalokadigital.entity.User;
import com.jeewaloka.digital.jeewalokadigital.mapper.UserMapper;
import com.jeewaloka.digital.jeewalokadigital.repository.UserRepository;
import com.jeewaloka.digital.jeewalokadigital.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

    @Override
    public void deleteByUserID(Long userid) {
        userRepository.deleteById(userid);
    }

    @Override
    @Transactional
    public List<UserResponseDTO> addUsers(List<UserResquestDTO> userDTOS) {
        List<User> usersToSave = userDTOS.stream()
                .map(userMapper::toUser)
                .toList();
        List<User> savedUsers = userRepository.saveAll(usersToSave);
        return savedUsers.stream()
                .map(userMapper::toDTO)
                .toList();
    }


    @Override
    public UserResponseDTO updateUser(UserResquestDTO userDTO, Long id) {
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        Optional<User> nuser = userRepository.findById(id);
        if(nuser.isPresent()){
            User puser = nuser.get();
            puser.setUname(userDTO.getUname());
            puser.setRole(userDTO.getRole());
            puser.setLastLogin(userDTO.getLastLogin());
            puser.setContact(userDTO.getContact());
            puser.setEmail(userDTO.getEmail());
            puser = userRepository.save(puser);
            userResponseDTO = userMapper.toDTO(puser);
        }
        return userResponseDTO;
    }

    @Override
    public List<UserResponseDTO> findAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDTO)
                .toList();
    }
}
