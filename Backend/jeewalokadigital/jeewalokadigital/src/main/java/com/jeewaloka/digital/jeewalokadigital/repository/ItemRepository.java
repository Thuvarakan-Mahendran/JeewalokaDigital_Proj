package com.jeewaloka.digital.jeewalokadigital.repository;

import com.jeewaloka.digital.jeewalokadigital.entity.GRN;
import com.jeewaloka.digital.jeewalokadigital.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item,Long> {

//        @Query("SELECT MAX(i.itemCode2) FROM Item i WHERE i.itemCode2 LIKE 'IT-%'")
//        String findLastItemCode2();
    }

