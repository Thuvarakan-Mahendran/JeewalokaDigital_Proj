package com.jeewaloka.digital.jeewalokadigital.repository;

import com.jeewaloka.digital.jeewalokadigital.entity.bill.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    List<Bill> findByDate(LocalDateTime date);
    @Query("SELECT b FROM Bill b WHERE " +
            "CAST(b.user.UID AS string) = :searchTerm OR " +
            "CAST(b.retailer.RetailerId AS string) = :searchTerm OR " +
            "CAST(b.BillNO AS string) = :searchTerm")
    List<Bill> searchByTerm(@Param("searchTerm") String searchTerm);

}
