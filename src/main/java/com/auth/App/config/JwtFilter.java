package com.auth.App.config;

import com.auth.App.services.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String reqHeader = request.getHeader("Authorization");
        if(reqHeader == null || !reqHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            filterChain.doFilter(request, response);
            return;
        }
        String token = reqHeader.substring(7);
        Claims claim = null;
        try{
            claim = jwtService.extractClaims(token);
        }catch (JwtException | IllegalArgumentException e){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        List<String> roles = (List<String>) claim.get("roles");
        List<GrantedAuthority> authorities = new ArrayList<>();

        for(String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }

        if(SecurityContextHolder.getContext().getAuthentication() == null) {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(claim.getSubject(), null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }
}
