package com.ajmayen.smartdepo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChalanItemResponse {

    private Long productId;
    private String productName;

    private Integer perCartonPieces;

    private Double pricePerCarton;
    private Integer cartonQty;
    private Integer freeCartonQty;

    private Double totalPrice;
}
