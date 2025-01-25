package com.jeewaloka.digital.jeewalokadigital.repository;

import com.jeewaloka.digital.jeewalokadigital.entity.bill.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    @Query("") //need to write query to retrieve list of Bills matching specific date and time
    List<Bill> findByDate(LocalDateTime issueDate); //it may has to be changed considering the way data is being stored
    @Query("")
    List<Bill> searchByTerm(String searchTerm); //need to check on the columns of string type
}
