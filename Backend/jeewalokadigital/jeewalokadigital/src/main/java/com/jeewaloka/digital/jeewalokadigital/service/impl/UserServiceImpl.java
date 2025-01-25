package com.jeewaloka.digital.jeewalokadigital.service.impl;

import com.jeewaloka.digital.jeewalokadigital.dto.UserDTO;
import com.jeewaloka.digital.jeewalokadigital.entity.User;
import com.jeewaloka.digital.jeewalokadigital.repository.UserRepository;
import com.jeewaloka.digital.jeewalokadigital.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public List<User> findByRole(String role) {
        return userRepository.findByRole(role);
    }
    public List<User> searchByTerm(String searchTerm){
        return userRepository.searchByTerm(searchTerm);
    }

    @Override
    public void deleteByUserID(Long userid) {
        userRepository.deleteById(userid);
    }

    @Override
    public List<User> addUsers(List<User> users) {
        List<User> userslist = userRepository.saveAll(users);
        return userslist;
    }
}
