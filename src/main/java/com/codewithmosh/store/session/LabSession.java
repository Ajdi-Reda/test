package com.codewithmosh.store.session;

import com.codewithmosh.store.equipments.Equipmentloan;
import com.codewithmosh.store.group.Group;
import com.codewithmosh.store.lab.Lab;
import com.codewithmosh.store.product.usage.ChemicalUsage;
import com.codewithmosh.store.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "lab_sessions")
public class LabSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lab_id", nullable = false)
    private Lab lab;

    @NotNull
    @Column(name = "scheduled_start", nullable = false)
    private Instant scheduledStart;

    @NotNull
    @Column(name = "scheduled_end", nullable = false)
    private Instant scheduledEnd;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @ColumnDefault("0")
    @Column(name = "validated")
    private Boolean validated;

    @OneToMany(mappedBy = "session")
    private Set<ChemicalUsage> chemicalUsages = new LinkedHashSet<>();

    @OneToMany(mappedBy = "session")
    private Set<Equipmentloan> equipmentLoans = new LinkedHashSet<>();

    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = new java.util.Date();
    }

}