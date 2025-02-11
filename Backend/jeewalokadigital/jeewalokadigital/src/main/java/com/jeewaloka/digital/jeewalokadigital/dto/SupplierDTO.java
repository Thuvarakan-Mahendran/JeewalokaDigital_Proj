package com.jeewaloka.digital.jeewalokadigital.dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter

public class SupplierDTO {

    private Long supplierId;
    private String supplierCode;
    private String supplierName;
    private String supplierContact;
    private String supplierEmail;
    private String supplierAddress;
    private String supplierFax;
    private String supplierWebsite;
    private String supplierStatus;


}
