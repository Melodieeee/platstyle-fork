package com.example.platstyle.repositories;

import com.example.platstyle.entities.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortfolioRepository  extends JpaRepository<Portfolio, Long> {
}
