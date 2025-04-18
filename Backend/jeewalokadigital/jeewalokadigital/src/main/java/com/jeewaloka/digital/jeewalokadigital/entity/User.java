package com.jeewaloka.digital.jeewalokadigital.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jeewaloka.digital.jeewalokadigital.entity.bill.Bill;
import com.jeewaloka.digital.jeewalokadigital.enums.UserRole;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class User {
    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idSeq")
//    @SequenceGenerator(name = "idSeq", sequenceName = "idSeq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private Long UID;
    @Column(name = "userName", nullable = false, unique = true)
    private String uname;   //This is name for display in their profile
    @Column(name = "userContact", nullable = false)
    private String contact;
    @Column(name = "userMail")
    private String email;
//    @Enumerated(EnumType.STRING)
//    @Column(name = "userRole", nullable = false)
//    private UserRole role;
    @Column(name = "LastLogin")
    private LocalDate lastLogin;
    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private UserCredentials userCredentials;
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Column(name = "BillNO")
    private List<Bill> bills;

//    public void addBill(Bill bill) {
//        bills.add(bill);
//        bill.setUser(this);
//    }
//
//    public void removeBill(Bill bill) {
//        bills.remove(bill);
//        bill.setUser(null);
//    }

    public User() {
    }

    public User(String uname, String contact, String email, UserCredentials userCredentials) {
        this.uname = uname;
        this.contact = contact;
        this.email = email;
        this.userCredentials = userCredentials;
    }

    public Long getUID() {
        return UID;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

//    public UserRole getRole() {
//        return role;
//    }
//
//    public void setRole(UserRole role) {
//        this.role = role;
//    }

    public LocalDate getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDate lastLogin) {
        this.lastLogin = lastLogin;
    }

    public List<Bill> getBills() {
        return bills;
    }

    public void setBills(List<Bill> bills) {
        this.bills = bills;
    }
}
