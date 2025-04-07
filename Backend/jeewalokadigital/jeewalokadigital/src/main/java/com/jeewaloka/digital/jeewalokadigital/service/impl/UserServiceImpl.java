package com.jeewaloka.digital.jeewalokadigital.service.impl;

import com.jeewaloka.digital.jeewalokadigital.dto.Request.UserResquestDTO;
import com.jeewaloka.digital.jeewalokadigital.dto.Response.UserResponseDTO;
import com.jeewaloka.digital.jeewalokadigital.entity.User;
import com.jeewaloka.digital.jeewalokadigital.repository.UserRepository;
import com.jeewaloka.digital.jeewalokadigital.service.UserService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public void deleteByUserID(Long userid) {
        userRepository.deleteById(userid);
    }

    @Override
    @Transactional
    public UserResponseDTO addUser(UserResquestDTO userDTO) {
        User user = modelMapper.map(userDTO,User.class);
        return modelMapper.map(userRepository.save(user),UserResponseDTO.class);
    }


    @Override
    public UserResponseDTO updateUser(UserResquestDTO userDTO, Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("user is not found with user Id " + id));
        if(user.getContact() != null) user.setContact(userDTO.getContact());
        if(user.getEmail() != null) user.setEmail(userDTO.getEmail());
        if(user.getUname() != null) user.setUname(userDTO.getUname());
        return modelMapper.map(userRepository.save(user),UserResponseDTO.class);
    }

    @Override
    public List<UserResponseDTO> findAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> modelMapper.map(user,UserResponseDTO.class))
                .collect(Collectors.toList());
    }
}
