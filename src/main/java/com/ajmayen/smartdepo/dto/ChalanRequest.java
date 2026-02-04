package com.ajmayen.smartdepo.dto;

import com.ajmayen.smartdepo.model.ChalanType;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ChalanRequest {

    private String chalanNo;
    private LocalDate chalanDate;

    private ChalanType type;

    private List<ChalanItemRequest> items;
}
