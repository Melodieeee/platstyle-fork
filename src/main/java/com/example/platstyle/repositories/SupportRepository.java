package com.example.platstyle.repositories;

import com.example.platstyle.entities.Support;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SupportRepository extends JpaRepository<Support, Long> {
    Optional<Support> findAllBySid(Long sid);

}
