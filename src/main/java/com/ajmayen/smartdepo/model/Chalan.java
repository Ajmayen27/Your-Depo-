package com.ajmayen.smartdepo.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chalans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Chalan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String chalanNo;

    @Enumerated(EnumType.STRING)
    private ChalanType chalanType;

    private Double subTotal;

    private List<ChalanItem> items = new ArrayList<>();
}
