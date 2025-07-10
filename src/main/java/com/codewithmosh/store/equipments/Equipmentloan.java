package com.codewithmosh.store.equipments;

import com.codewithmosh.store.session.LabSession;
import com.codewithmosh.store.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "equipmentloans")
public class Equipmentloan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_id")
    private Equipment equipment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "reserved_from")
    private Instant reservedFrom;

    @Column(name = "reserved_to")
    private Instant reservedTo;

    @Size(max = 255)
    @Column(name = "signature")
    private String signature;

    @Column(name = "returned")
    private Boolean returned;

    @Column(name = "expected_return_date")
    private Instant expectedReturnDate;

    @Column(name = "actual_return_date")
    private Instant actualReturnDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checkout_by")
    private User checkoutBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "returned_to")
    private User returnedTo;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "session_id", nullable = false)
    private LabSession session;

    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = new java.util.Date();
    }

}