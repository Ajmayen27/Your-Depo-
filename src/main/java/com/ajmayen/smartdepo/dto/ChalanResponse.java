package com.ajmayen.smartdepo.dto;

import com.ajmayen.smartdepo.model.ChalanType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class ChalanResponse {

    private Long id;

    private String chalanNo;
    private LocalDate chalanDate;

    private ChalanType type;

    private Double subTotal;

    private List<ChalanItemResponse> items;
}
