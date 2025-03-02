package com.jeewaloka.digital.jeewalokadigital.repository;

import com.jeewaloka.digital.jeewalokadigital.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item,Long> {
    Item findByItemNameIgnoreCase(String itemName);


//        @Query("SELECT MAX(i.itemCode2) FROM Item i WHERE i.itemCode2 LIKE 'IT-%'")
//        String findLastItemCode2();
    }

