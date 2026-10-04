package com.auth.App.services;
import com.auth.App.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.io.Decoders;
import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {

    private  final String SECRET = "zLhTs7mCFPMMsFiXDEdg70jWYCnhbpnbRjhLTU/SqPY=";

    private  final SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET));

    private  final JwtParser parser = Jwts.parser().verifyWith(secretKey).build();

    public String generateToken(UserDetails userDetails) {
        String userName = userDetails.getUsername();
        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return   Jwts.builder()
                .subject(userName)
                .issuedAt(new Date())
                .expiration(Date.from(Instant.now().plus(Duration.ofMinutes(15))))
                .claim("roles", roles)
                .signWith(secretKey)
                .compact();
    }

    public Claims extractClaims(String token) {
        return  parser
                .parseSignedClaims(token)
                .getPayload();
    }


}
