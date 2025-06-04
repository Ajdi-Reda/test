package com.codewithmosh.store.invitation;

import com.codewithmosh.store.user.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
@AllArgsConstructor
public class InvitationTokenService {
    private InvitationTokenRepository  invitationTokenRepository;
    private static final SecureRandom secureRandom = new SecureRandom(); // strong RNG
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder().withoutPadding();
    public Iterable<InvitationToken> getAllInvitationTokens() {
        return invitationTokenRepository.findAll();
    }

    public String createInvitationToken(User user) {
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        String token =  base64Encoder.encodeToString(randomBytes);

        InvitationToken it = new InvitationToken();
        it.setToken(token);
        it.setUser(user);
        invitationTokenRepository.save(it);
        return token;
    }
}
