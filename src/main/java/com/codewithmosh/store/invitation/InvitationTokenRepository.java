package com.codewithmosh.store.invitation;

import com.codewithmosh.store.subject.Subject;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;



public interface InvitationTokenRepository extends JpaRepository<InvitationToken, Integer> {
    Optional<InvitationToken> findByToken(String token);

    void deleteByToken(@NotBlank(message = "Token is required") String token);
    List<InvitationToken> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

}