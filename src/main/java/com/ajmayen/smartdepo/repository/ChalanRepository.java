package com.ajmayen.smartdepo.repository;

import com.ajmayen.smartdepo.model.Chalan;
import com.ajmayen.smartdepo.model.ChalanType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChalanRepository extends JpaRepository<Chalan, Long> {
    Optional<Chalan> findTopByDealerIdOrderByIdDesc(Long dealerId);
}
