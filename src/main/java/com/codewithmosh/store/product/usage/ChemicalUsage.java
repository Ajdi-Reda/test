package com.codewithmosh.store.product.usage;

import com.codewithmosh.store.product.item.ChemicalProduct;
import com.codewithmosh.store.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "chemical_usage")
public class ChemicalUsage {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ChemicalProduct product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "taken_by", referencedColumnName = "id")
    private User user;

    @Column(name = "amount")
    private Float amount;

    @Column(name = "date")
    private LocalDate date;

    @Lob
    @Column(name = "purpose")
    private String purpose;

    @Lob
    @Column(name = "status")
    private String status = "REQUESTED";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "handled_by", referencedColumnName = "id")
    private User handledBy;

    @Column(name = "handled_at")
    private Instant handledAt = Instant.now();

}