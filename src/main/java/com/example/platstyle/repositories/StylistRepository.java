package com.example.platstyle.repositories;

import com.example.platstyle.entities.Stylist;
import com.example.platstyle.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.Optional;

public interface StylistRepository  extends JpaRepository<Stylist, Long> {
    Optional<Stylist> findAllByUid(User uid);
    @Transactional
    void deleteAllByUid(User uid);
}
