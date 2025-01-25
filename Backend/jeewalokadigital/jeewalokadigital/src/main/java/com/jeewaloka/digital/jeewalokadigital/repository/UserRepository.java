package com.jeewaloka.digital.jeewalokadigital.repository;

import com.jeewaloka.digital.jeewalokadigital.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("") //need to define the query to retrieve by role
    List<User> findByRole(@Param("role") String role);
    @Query("SELECT u FROM User u WHERE " +
            "LOWER(u.uname) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "u.contact LIKE CONCAT('%', :searchTerm, '%')") //here, need to check the u."name that i am defining"
    List<User> searchByTerm(@Param("searchTerm") String searchTerm);
}
