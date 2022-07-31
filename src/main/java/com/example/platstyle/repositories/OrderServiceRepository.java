package com.example.platstyle.repositories;

import com.example.platstyle.entities.Order_service;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderServiceRepository extends JpaRepository<Order_service, Long> {
}
