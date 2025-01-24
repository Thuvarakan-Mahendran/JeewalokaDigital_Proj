package com.jeewaloka.digital.jeewalokadigital.service;

import com.jeewaloka.digital.jeewalokadigital.entity.User;

public interface UserService{
    User findByRole(String role);
}
