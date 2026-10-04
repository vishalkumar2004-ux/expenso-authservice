package com.auth.App.controller;
import com.auth.App.entities.Token;
import com.auth.App.repositories.RefTokenRepository;
import com.auth.App.repositories.UserRepository;
import com.auth.App.request.AuthDTO;
import com.auth.App.request.RefreshTokenDTO;
import com.auth.App.response.TokenResponeDTO;
import com.auth.App.services.JwtService;
import com.auth.App.services.RefTokenService;
import com.auth.App.services.UserDetailsServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@RestController
@Controller
public class TokenController {

    private final JwtService jwtService;

    private final UserDetailsServiceImpl userDetailsService;

    private final RefTokenService refTokenService;

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final RefTokenRepository refTokenRepository;

    public TokenController(JwtService jwtService, UserDetailsServiceImpl userDetailsService, RefTokenService refTokenService, AuthenticationManager authenticationManager, UserRepository userRepository, RefTokenRepository refTokenRepository) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.refTokenService = refTokenService;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.refTokenRepository = refTokenRepository;
    }

    @PostMapping("/auth/v1/login")
    public ResponseEntity login(@RequestBody AuthDTO authDto) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(authDto.getUsername(),authDto.getPassword());
        Authentication authObj = authenticationManager.authenticate(authToken);

        if(authObj.isAuthenticated()){
            Token token = refTokenRepository.findByUserUsername(authDto.getUsername());
            String newRefreshToken = UUID.randomUUID().toString();
            Instant newExpiryInstant = Instant.now().plus(Duration.ofDays(10));

            token.setRefreshToken(newRefreshToken);
            token.setExpiry(newExpiryInstant);
            String jwtToken = jwtService.generateToken(userDetailsService.loadUserByUsername(authDto.getUsername()));
            refTokenRepository.save(token);

            return new ResponseEntity<>(TokenResponeDTO.builder().refreshToken(newRefreshToken).jwtToken(jwtToken).build(), HttpStatus.OK);
        }
        return new ResponseEntity<>("wrong credentials", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/auth/v1/refreshToken")
    public ResponseEntity refreshToken(@RequestBody RefreshTokenDTO refreshTokenDTO){
        Token token;
        UserDetails userDetails;
        try{
             token = refTokenRepository.findByRefreshToken(refreshTokenDTO.getToken());
             userDetails = userDetailsService.loadUserByUsername(token.getUser().getUsername());
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>("invalid token", HttpStatus.BAD_REQUEST);
        }
        if(token.getExpiry().isBefore(Instant.now())) {
            return new ResponseEntity<>("pls login again", HttpStatus.BAD_REQUEST);
        }

        String jwtToken = jwtService.generateToken(userDetails);
        return new ResponseEntity<>(TokenResponeDTO.builder().refreshToken(token.getRefreshToken()).jwtToken(jwtToken).build(), HttpStatus.OK);
    }
}
