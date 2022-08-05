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

public interface StylistRepository  extends JpaRepository<Stylist, Long>, StylistRepositoryCustom {

    Optional<Stylist> findAllBySid(Long sid);
    Optional<Stylist> findAllByUid(User uid);
    @Query(value = "select uid from stylist  where sid = ?1 ",nativeQuery = true)
    long findUidBySid (long sid);

    @Query(value = "select * from stylist  where verify = 1 ",nativeQuery = true)
    List<Stylist> findAllVerifiedStylist();
    @Modifying
    @Query(value="delete from stylist where uid = ?1", nativeQuery = true)
    @Transactional
    void deleteByUid(User user);
    Optional<Stylist> findBySid(long sid);
    @Query(value = "select * from stylist  where name like %?1%",nativeQuery = true)
    List<Stylist> findAllByName(String name);
}
