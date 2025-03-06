package com.jeewaloka.digital.jeewalokadigital.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class UserCredentials {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long UserCredID;
    @Column(unique = true)
    private String username;    //This is credential to login into application
    private String password;
    @OneToOne(mappedBy = "userCredentials", cascade = CascadeType.ALL)
    private User user;
    public UserCredentials() {
    }
    public UserCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Long getUserCredID() {
        return UserCredID;
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
