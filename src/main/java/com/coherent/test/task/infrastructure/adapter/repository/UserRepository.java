package com.coherent.test.task.infrastructure.adapter.repository;

import com.coherent.test.task.domain.model.HotelUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<HotelUser, Integer> {

    @Query(value = "SELECT * FROM hotel_user WHERE user_login = ?", nativeQuery = true)
    Optional<HotelUser> findByUserLogin(String userLogin);

}
