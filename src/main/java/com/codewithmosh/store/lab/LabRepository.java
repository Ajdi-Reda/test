package com.codewithmosh.store.lab;

import com.codewithmosh.store.subject.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LabRepository extends JpaRepository<Lab, Integer> {
    List<Lab> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
