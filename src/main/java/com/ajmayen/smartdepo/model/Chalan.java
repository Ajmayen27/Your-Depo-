package com.ajmayen.smartdepo.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    private String trackNo;

    private String chalanNo;

    private LocalDate chalanDate;

    @Enumerated(EnumType.STRING)
    private ChalanType type;

    private Double subTotal;

    @ManyToOne(optional = true)
    private Dealer dealer;

    private Double dealerDue;

    private Double depoDue;

    @OneToMany(
            mappedBy = "chalan",
            cascade = CascadeType.ALL
    )
    private List<ChalanItem> items = new ArrayList<>();

}
