package com.jeewaloka.digital.jeewalokadigital.service;

import com.jeewaloka.digital.jeewalokadigital.dto.UserDTO;
import com.jeewaloka.digital.jeewalokadigital.entity.User;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface UserService{
    List<User> findByRole(String role);
    List<User> searchByTerm(String searchTerm);
    void deleteByUserID(Long userid);
    List<User> addUsers(List<User> users);
}
