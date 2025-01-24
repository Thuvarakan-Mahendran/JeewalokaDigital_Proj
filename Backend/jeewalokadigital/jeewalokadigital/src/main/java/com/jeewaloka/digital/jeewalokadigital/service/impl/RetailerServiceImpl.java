package com.jeewaloka.digital.jeewalokadigital.service.impl;
import com.jeewaloka.digital.jeewalokadigital.repository.RetailerRepo;
import com.jeewaloka.digital.jeewalokadigital.service.RetailerService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class RetailerServiceImpl implements RetailerService {
    @Autowired
    private RetailerRepo retailerRepo;
}