package com.ajmayen.smartdepo.service;

import com.ajmayen.smartdepo.dto.ChalanRequest;
import com.ajmayen.smartdepo.dto.ChalanResponse;

public interface ChalanService {

    ChalanResponse createChalan(ChalanRequest chalanRequest);
}
