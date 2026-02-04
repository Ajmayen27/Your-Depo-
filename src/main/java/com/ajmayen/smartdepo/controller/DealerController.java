package com.ajmayen.smartdepo.controller;

import com.ajmayen.smartdepo.model.Dealer;
import com.ajmayen.smartdepo.repository.DealerRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dealers")
public class DealerController {

    private final DealerRepository dealerRepository;

    public DealerController(DealerRepository dealerRepository) {
        this.dealerRepository = dealerRepository;
    }

    @PostMapping
    public Dealer create(@RequestBody Dealer dealer) {
        return dealerRepository.save(dealer);
    }

    @GetMapping
    public List<Dealer> getAll() {
        return dealerRepository.findAll();
    }
}
