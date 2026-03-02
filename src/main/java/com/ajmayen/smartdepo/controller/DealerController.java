package com.ajmayen.smartdepo.controller;

import com.ajmayen.smartdepo.dto.DealerRequest;
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

    @PutMapping("/{id}")
    public Dealer update(@PathVariable Long id, @RequestBody DealerRequest  dealerRequest) {

        Dealer dealer = dealerRepository.findById(id).orElseThrow(() -> new RuntimeException("Dealer not found"));

        dealer.setName(dealerRequest.getName());
        dealer.setPhone(dealerRequest.getPhone());
        dealer.setAddress(dealerRequest.getAddress());

        return dealerRepository.save(dealer);
    }


    @DeleteMapping("/{id}")
    public  String  delete(@PathVariable Long id) {
        dealerRepository.deleteById(id);
        return "Dealer with id " + id + " deleted successfully";
    }


}
