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

//    @JsonIgnore
//    @ManyToMany(mappedBy = "roles")
//    private List<User> users = new ArrayList<>();
}