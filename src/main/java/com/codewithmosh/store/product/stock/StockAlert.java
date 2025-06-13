package com.codewithmosh.store.product.stock;

import com.codewithmosh.store.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "stock_alerts")
public class StockAlert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "item_id")
    private Integer itemId;

    @ColumnDefault("(curdate())")
    @Column(name = "date")
    private LocalDate date;

    @ColumnDefault("0")
    @Column(name = "resolved")
    private Boolean resolved;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resolved_by")
    private User resolvedBy;

}