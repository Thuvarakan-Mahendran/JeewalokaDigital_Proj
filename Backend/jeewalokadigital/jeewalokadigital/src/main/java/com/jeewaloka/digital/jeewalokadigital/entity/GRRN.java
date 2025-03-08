package com.jeewaloka.digital.jeewalokadigital.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Good_Return_Note")
public class GRRN {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long grrnid;

    @Column(nullable = false)
    private String supplierName;

    @Column(nullable = false)
    private LocalDate returneDate;


    @OneToMany(mappedBy = "grrn", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<GRRNItem> grrnItemList;




}
