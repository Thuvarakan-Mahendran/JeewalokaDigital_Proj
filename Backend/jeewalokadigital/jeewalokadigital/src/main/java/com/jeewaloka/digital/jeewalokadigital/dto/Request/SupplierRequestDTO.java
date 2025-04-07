package com.jeewaloka.digital.jeewalokadigital.dto.Request;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SupplierRequestDTO {
    private String supplierCode;
    private String supplierName;
    private String supplierContact;
    private String supplierEmail;
    private String supplierAddress;
    private String supplierFax;
    private String supplierWebsite;
    private String supplierStatus;
}
