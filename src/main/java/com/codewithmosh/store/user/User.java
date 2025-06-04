package com.codewithmosh.store.user;

import com.codewithmosh.store.role.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(max = 30)
    @Column(name = "name", length = 30)
    private String name;

    @Size(max = 255)
    @Column(name = "email")
    private String email;

    @Lob
    @Column(name = "password")
    private String password;

    @Column(name = "first_login")
    private boolean firstLogin = true;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new LinkedHashSet<>();

    public Boolean checkRoleExists(Integer id) {
        return roles.stream().anyMatch(role -> role.getId().equals(id));
    }

    public void addRole(Role role) {
        roles.add(role);
    }

//    @OneToMany(mappedBy = "user")
//    private Set<ChemicalUsage> usages;
}