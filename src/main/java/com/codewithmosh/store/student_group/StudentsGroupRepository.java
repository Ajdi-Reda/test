package com.codewithmosh.store.student_group;

import com.codewithmosh.store.subject.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface StudentsGroupRepository extends JpaRepository<StudentsGroup, Integer> {
    List<StudentsGroup> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
