package com.auth.App.services;

import com.auth.App.entities.Token;
import com.auth.App.entities.UserInfo;
import com.auth.App.repositories.RefTokenRepository;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
public class RefTokenService {

    private final RefTokenRepository refTokenRepository;

    public RefTokenService(RefTokenRepository refTokenRepository) {
        this.refTokenRepository = refTokenRepository;
    }

    public String createRefToken( UserInfo user) {
        String refToken = UUID.randomUUID().toString();
        Instant expiryTime = Instant.now().plus(Duration.ofDays(10));
        Token token = Token.builder()
                .refreshToken(refToken)
                .expiry(expiryTime)
                .user(user)
                .build();
        refTokenRepository.save(token);
        return token.getRefreshToken();
    }

}
