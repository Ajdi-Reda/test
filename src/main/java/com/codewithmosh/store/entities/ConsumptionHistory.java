package com.codewithmosh.store.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
public class ConsumptionHistory {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ChemicalProduct product;

    @NotNull
    @Column(name = "date", nullable = false)
    private Instant date;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Float quantity;

    @Lob
    @Column(name = "purpose")
    private String purpose;

    @Lob
    @Column(name = "status")
    private String status;

    @Column(name = "handled_at")
    private Instant handledAt;

    @NotNull
    @Lob
    @Column(name = "action_type", nullable = false)
    private String actionType;

    @NotNull
    @Lob
    @Column(name = "notes", nullable = false)
    private String notes;

}