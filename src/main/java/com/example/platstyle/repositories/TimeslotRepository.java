package com.example.platstyle.repositories;

import com.example.platstyle.entities.Timeslot;
import com.example.platstyle.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface TimeslotRepository extends JpaRepository<Timeslot, Long> {

    @Query(value = "select * from timeslot where uid = ?1",nativeQuery = true)
    List<Timeslot> findAllByUid (long uid);
    @Transactional
    long deleteByUser(User user);


}
