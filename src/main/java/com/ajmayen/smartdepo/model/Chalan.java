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

    @OneToMany(
            mappedBy = "chalan",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ChalanItem> items = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "dealer_id")
    private Dealer dealer;

    // Phase 3 Fields
    private Double dealerDue;
    private Double depoDue;

}
