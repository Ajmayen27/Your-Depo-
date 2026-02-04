package com.ajmayen.smartdepo.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "dealer_deposits")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DealerDeposit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Dealer Relation
    @ManyToOne
    @JoinColumn(name = "dealer_id")
    private Dealer dealer;

    private Double amount;

    private LocalDate depositDate;
}
