package com.example.platstyle.repositories;

import com.example.platstyle.entities.Service;
import com.example.platstyle.entities.Stylist;
import com.example.platstyle.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface StylistRepository  extends JpaRepository<Stylist, Long> {

    Optional<Stylist> findAllBySid(Long sid);
    Optional<Stylist> findAllByUid(User uid);
    @Query(value = "select uid from service  where sid = ?1 ",nativeQuery = true)
    long findUidBySid (long sid);
    @Modifying
    @Query(value="delete from stylist where uid = ?1", nativeQuery = true)
    @Transactional
    void deleteByUid(User user);
}
