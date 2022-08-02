package com.example.platstyle.repositories;

import com.example.platstyle.entities.Order_service;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderServiceRepository extends JpaRepository<Order_service, Long> {
    @Override
    Optional<Order_service> findById(Long aLong);
}
