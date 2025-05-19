package com.codewithmosh.store.repositories;

import com.codewithmosh.store.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}