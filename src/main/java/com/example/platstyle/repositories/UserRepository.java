package com.example.platstyle.repositories;

import com.example.platstyle.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findAllByUid(Long uid);

}
