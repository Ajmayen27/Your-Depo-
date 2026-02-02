package com.ajmayen.smartdepo.dto;

import lombok.Data;

@Data
public class ProductRequest {

    private Long categoryId;
    private String name;
    private String skuCode;
    private Integer perCartonPieces;
    private Double pricePerCarton;
}
