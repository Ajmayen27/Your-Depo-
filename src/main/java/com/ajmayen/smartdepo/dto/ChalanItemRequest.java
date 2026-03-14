package com.ajmayen.smartdepo.dto;

import lombok.Data;

@Data
public class ChalanItemRequest {

    private Long productId;
    private Integer cartonQty;
    private Integer freeCartonQty;
    private Double pricePerCarton;
}
