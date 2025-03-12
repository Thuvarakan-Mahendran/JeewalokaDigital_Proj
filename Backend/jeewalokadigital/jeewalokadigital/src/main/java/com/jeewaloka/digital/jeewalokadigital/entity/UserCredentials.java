package com.jeewaloka.digital.jeewalokadigital.entity;

import jakarta.persistence.*;

@Entity
public class UserCredentials {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long UserCredID;
    @Column(unique = true)
    private String username;    //This is credential to login into application
    private String password;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userID", referencedColumnName = "userId")
    private User user;
    public UserCredentials() {
    }

    public UserCredentials(String username, String password, User user) {
        this.username = username;
        this.password = password;
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
