package com.jeewaloka.digital.jeewalokadigital.entity;

import com.jeewaloka.digital.jeewalokadigital.enums.UserRole;
import jakarta.persistence.*;

@Entity
public class UserCredentials {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long UserCredID;
    @Column(unique = true)
    private String username;    //This is credential to login into application
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(name = "userRole", nullable = false)
    private UserRole role;
    @OneToOne//(cascade = CascadeType.DETACH)
    @JoinColumn(name = "userID", referencedColumnName = "userId")
    private User user;
    public UserCredentials() {
    }

    public UserCredentials(String username, String password, UserRole role) {
        this.username = username;
        this.password = password;
        this.role = role;
//        this.user = user;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
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
