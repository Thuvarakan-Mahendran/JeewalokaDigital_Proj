package com.jeewaloka.digital.jeewalokadigital.repository;

import com.jeewaloka.digital.jeewalokadigital.entity.GRRN;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories

public interface GRRNRepository extends JpaRepository<GRRN, Long> {
    // Custom query to find GRRNs by supplier name (if needed)
   // List<GRRN> findBySupplierName(String supplierName);
}
