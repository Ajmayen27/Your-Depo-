package com.ajmayen.smartdepo.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
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

    private LocalDate chalanDate;

    @Enumerated(EnumType.STRING)
    private ChalanType type;

    private Double subTotal;

    private List<ChalanItem> items = new ArrayList<>();
}
