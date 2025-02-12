package com.jeewaloka.digital.jeewalokadigital.repository;

import com.jeewaloka.digital.jeewalokadigital.entity.UserCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//@RepositoryRestResource(exported = false)
@Repository
public interface UserCredRepository extends JpaRepository<UserCredentials, Long> {
    Optional<UserCredentials> findByUsername(String username);

    @Override
    Optional<UserCredentials> findById(Long aLong);
}
