package com.ajmayen.smartdepo.controller;

import com.ajmayen.smartdepo.dto.ChalanRequest;
import com.ajmayen.smartdepo.dto.ChalanResponse;
import com.ajmayen.smartdepo.service.ChalanService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chalans")
public class ChalanController {

    private final ChalanService chalanService;

    public ChalanController(ChalanService chalanService) {
        this.chalanService = chalanService;
    }

    @PostMapping("/incoming")
    public ChalanResponse createIncoming(@RequestBody ChalanRequest request) {
        return chalanService.createChalan(request);
    }

    @PostMapping("/outgoing")
    public ChalanResponse createOutgoing(@RequestBody ChalanRequest request) {
        return chalanService.createChalan(request);
    }
}
