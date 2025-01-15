package com.jeewaloka.digital.jeewalokadigital.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Seller {
    @Id

    private int sellerId;
    private String sellerName;
    private String sellerTelephone;
    private String sellerEmail;
    private String sellerAddress;

  }