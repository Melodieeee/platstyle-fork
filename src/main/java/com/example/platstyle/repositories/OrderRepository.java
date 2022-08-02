package com.example.platstyle.repositories;

import com.example.platstyle.entities.Order;
import com.example.platstyle.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(value = "select * from orders where uid = ?1 and status != 0",nativeQuery = true)
    List<Order> findAllByUid(long uid);
    Optional<Order> findAllByUserAndStatus(User user, int status);
}
