package com.jeewaloka.digital.jeewalokadigital.repository;

import com.jeewaloka.digital.jeewalokadigital.entity.GRN;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GRNRepository extends JpaRepository<GRN, Long> {
    List<GRN> findByGrnStatus(String status);

    @Query("SELECT MAX(g.grnCode) FROM GRN g WHERE g.grnCode LIKE 'GRN-%'")
    String findLastGRNCode();
}
