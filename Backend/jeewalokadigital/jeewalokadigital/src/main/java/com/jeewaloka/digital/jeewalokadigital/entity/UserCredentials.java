package com.jeewaloka.digital.jeewalokadigital.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class UserCredentials {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long UID;
    private String username;
    private String password;
    public UserCredentials() {
    }
    public UserCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Long getUID() {
        return UID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
