package com.codewithmosh.store.group;

import com.codewithmosh.store.subject.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;




public interface GroupRepository extends JpaRepository<Group, Integer> {
    List<Group> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
