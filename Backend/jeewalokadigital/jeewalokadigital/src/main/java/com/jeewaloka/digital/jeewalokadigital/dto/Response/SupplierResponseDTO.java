package com.jeewaloka.digital.jeewalokadigital.dto.Response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupplierResponseDTO {
    private Long supplierId;
    private String supplierCode;
    private String supplierName;
    private String supplierContact;
    private String supplierEmail;
    private String supplierAddress;
    private String supplierFax;
    private String supplierWebsite;
    private String supplierStatus;
    private String supplierCreatedDate;
}
