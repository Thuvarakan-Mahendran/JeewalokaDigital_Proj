package com.jeewaloka.digital.jeewalokadigital.dto;


import com.jeewaloka.digital.jeewalokadigital.entity.Retailer;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "api/v1")
public class InvoiceDTO {
    private List<AbstractReadWriteAccess.Item> items;
    private int total;
    private int discount;
    private Retailer shop_name;
}
