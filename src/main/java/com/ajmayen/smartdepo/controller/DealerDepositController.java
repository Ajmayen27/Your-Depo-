package com.ajmayen.smartdepo.controller;

import com.ajmayen.smartdepo.model.DealerDeposit;
import com.ajmayen.smartdepo.service.DealerDepositService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deposits")
public class DealerDepositController {

    private final DealerDepositService depositService;

    public DealerDepositController(DealerDepositService depositService) {
        this.depositService = depositService;
    }

    @PostMapping
    public DealerDeposit addDeposit(@RequestBody DealerDeposit deposit) {
        return depositService.addDeposit(deposit);
    }

    @GetMapping("/{dealerId}")
    public List<DealerDeposit> getDealerDeposit(@PathVariable Long dealerId) {
        return  depositService.getIndividualDeposit(dealerId);
    }


    @GetMapping("/total/{dealerId}")
    public Double getTotal(@PathVariable Long dealerId) {
        return depositService.getTotalDeposit(dealerId);
    }

}
