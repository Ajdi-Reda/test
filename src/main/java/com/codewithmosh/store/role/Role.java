package com.codewithmosh.store.role;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(max = 30)
    @Column(name = "name", length = 30)
    private String name;

    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = new java.util.Date();
    }

//    @JsonIgnore
//    @ManyToMany(mappedBy = "roles")
//    private List<User> users = new ArrayList<>();
}