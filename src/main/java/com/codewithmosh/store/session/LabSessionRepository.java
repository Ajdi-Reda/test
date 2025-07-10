package com.codewithmosh.store.session;

import com.codewithmosh.store.subject.Subject;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import java.util.List;

public interface LabSessionRepository extends JpaRepository<LabSession, Integer> {
    List<LabSession> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);


    @EntityGraph(attributePaths = {"equipmentLoans", "chemicalUsages"})
    List<LabSession> findAll();
    // Find sessions for the same lab that overlap with the given time range
    @Query("SELECT ls FROM LabSession ls WHERE ls.lab.id = :labId AND (:newStart < ls.scheduledEnd AND :newEnd > ls.scheduledStart) AND (:excludeId IS NULL OR ls.id <> :excludeId)")
    Optional<LabSession> findOverlappingSession(@Param("labId") Integer labId,
                                                @Param("newStart") Instant newStart,
                                                @Param("newEnd") Instant newEnd,
                                                @Param("excludeId") Integer excludeId);

}
