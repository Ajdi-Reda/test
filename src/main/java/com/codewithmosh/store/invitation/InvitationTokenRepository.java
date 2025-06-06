package com.codewithmosh.store.invitation;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvitationTokenRepository extends JpaRepository<InvitationToken, Integer> {
    Optional<InvitationToken> findByToken(String token);

    void deleteByToken(@NotBlank(message = "Token is required") String token);
}