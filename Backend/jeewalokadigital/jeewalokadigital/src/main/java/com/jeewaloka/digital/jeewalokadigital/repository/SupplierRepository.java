package com.jeewaloka.digital.jeewalokadigital.repository;

import com.jeewaloka.digital.jeewalokadigital.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier,Long>
{
    @Query("SELECT MAX(s.supplierCode) FROM Supplier s WHERE s.supplierCode LIKE 'SUP-%'")
    String findLastSupplierCode();

    Supplier findBySupplierNameIgnoreCase(String supplierName);
}
