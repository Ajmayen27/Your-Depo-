package com.ajmayen.smartdepo.service;

import com.ajmayen.smartdepo.model.DealerDeposit;
import com.ajmayen.smartdepo.repository.DealerDepositRepository;
import org.springframework.stereotype.Service;

@Service
public class DealerDepositService {

    private final DealerDepositRepository depositRepository;

    public DealerDepositService(DealerDepositRepository depositRepository) {
        this.depositRepository = depositRepository;
    }

    public Double getTotalDeposit(Long dealerId) {

        return depositRepository.findByDealerId(dealerId)

                
                .stream()
                .mapToDouble(DealerDeposit::getAmount)
                .sum();
    }

    public DealerDeposit addDeposit(DealerDeposit deposit) {
        return depositRepository.save(deposit);
    }
}
