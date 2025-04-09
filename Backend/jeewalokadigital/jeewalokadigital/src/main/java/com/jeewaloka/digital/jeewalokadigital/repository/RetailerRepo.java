package com.jeewaloka.digital.jeewalokadigital.repository;

import com.jeewaloka.digital.jeewalokadigital.entity.Retailer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional; // <-- Import Optional

@Repository
public interface RetailerRepo extends JpaRepository<Retailer, String> {
    // --- NEW METHOD SIGNATURE ---
    Optional<Retailer> findByRetailerEmail(String email); // Used for checking duplicates
}