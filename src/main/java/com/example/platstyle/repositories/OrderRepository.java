package com.example.platstyle.repositories;

import com.example.platstyle.entities.Order;
import com.example.platstyle.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findAllByUserAndStatus(User user, int status);
}
