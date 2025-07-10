package com.codewithmosh.store.role;

import com.codewithmosh.store.subject.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    List<Role> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}