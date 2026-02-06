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

    public OutgoingChalanService(
            ChalanRepository chalanRepository,
            StockRepository stockRepository,
            DealerDepositService depositService) {
        this.chalanRepository = chalanRepository;
        this.stockRepository = stockRepository;
        this.depositService = depositService;
    }

    public Chalan processOutgoingChalan(Chalan chalan) {

        Long dealerId = chalan.getDealer().getId();

        double totalDeposit = depositService.getTotalDeposit(dealerId);
        double currentTotal = chalan.getSubTotal();

        double previousDepoDue = chalanRepository
                .findTopByDealerIdOrderByIdDesc(dealerId)
                .map(c -> c.getDepoDue() == null ? 0.0 : c.getDepoDue())
                .orElse(0.0);

        double grandTotal = currentTotal + previousDepoDue;

        if (grandTotal > totalDeposit) {
            chalan.setDepoDue(grandTotal - totalDeposit);
            chalan.setDealerDue(0.0);
        } else {
            chalan.setDealerDue(totalDeposit - grandTotal);
            chalan.setDepoDue(0.0);
        }

        // Deduct stock
        for (ChalanItem item : chalan.getItems()) {
            Stock stock = stockRepository.findByProductId(
                    item.getProduct().getId()
            ).orElseThrow();

            stock.setCurrentCartonQty(
                    stock.getCurrentCartonQty() - item.getCartonQty()
            );

            stockRepository.save(stock);
        }

        return chalanRepository.save(chalan);
    }
}
