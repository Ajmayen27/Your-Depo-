package com.ajmayen.smartdepo.service;

import com.ajmayen.smartdepo.model.Chalan;
import com.ajmayen.smartdepo.model.ChalanItem;
import com.ajmayen.smartdepo.model.Dealer;
import com.ajmayen.smartdepo.model.Stock;
import com.ajmayen.smartdepo.repository.ChalanRepository;
import com.ajmayen.smartdepo.repository.StockRepository;
import org.springframework.stereotype.Service;

@Service
public class OutgoingChalanService {

    private final ChalanRepository chalanRepository;
    private final StockRepository stockRepository;
    private final DealerDepositService depositService;

    public OutgoingChalanService(ChalanRepository chalanRepository, StockRepository stockRepository, DealerDepositService depositService) {
        this.chalanRepository = chalanRepository;
        this.stockRepository = stockRepository;
        this.depositService = depositService;
    }

    public Chalan createOutgoingChalan(Chalan chalan) {

        Dealer dealer = chalan.getDealer();

        // 1. Total Deposit
        Double totalDeposit = depositService.getTotalDeposit(dealer.getId());

        // 2. Current Total
        Double currentTotal = chalan.getSubTotal();

        // 3. Previous Depo Due (from last outgoing)
        Double previousDepoDue = getLastDepoDue(dealer.getId());

        // 4. Grand Total
        Double grandTotal = currentTotal + previousDepoDue;

        // 5. Apply Rule
        if (grandTotal > totalDeposit) {
            chalan.setDepoDue(grandTotal - totalDeposit);
            chalan.setDealerDue(0.0);
        } else {
            chalan.setDealerDue(totalDeposit - grandTotal);
            chalan.setDepoDue(0.0);
        }

        // 6. Deduct Stock
        for (ChalanItem item : chalan.getItems()) {

            Stock stock = stockRepository.findByProductId(item.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Stock not found"));

            stock.setCurrentCartonQty(
                    stock.getCurrentCartonQty() - item.getCartonQty()
            );

            stockRepository.save(stock);
        }

        return chalanRepository.save(chalan);
    }

    private Double getLastDepoDue(Long dealerId) {

        return chalanRepository.findTopByDealerIdOrderByIdDesc(dealerId)
                .map(Chalan::getDepoDue)
                .orElse(0.0);
    }
}
