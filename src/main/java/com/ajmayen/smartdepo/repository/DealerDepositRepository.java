package com.ajmayen.smartdepo.repository;

import com.ajmayen.smartdepo.model.DealerDeposit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DealerDepositRepository extends JpaRepository<DealerDeposit, Long> {
    List<DealerDeposit> findByDealerId(Long dealerId);
}
