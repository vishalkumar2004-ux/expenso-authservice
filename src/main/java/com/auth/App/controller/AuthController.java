package com.auth.App.controller;

import com.auth.App.entities.UserInfo;
import com.auth.App.request.SignupDTO;
import com.auth.App.response.TokenResponeDTO;
import com.auth.App.services.AuthService;
import com.auth.App.services.JwtService;
import com.auth.App.services.RefTokenService;
import com.auth.App.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController
public class AuthController {

    @Autowired
    private final AuthService authService;

    @Autowired
    private final JwtService jwtService;

    @Autowired
    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    private final RefTokenService refTokenService;

    public AuthController(AuthService authService, JwtService jwtService, UserDetailsServiceImpl userDetailsService, RefTokenService refTokenService) {
        this.authService = authService;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.refTokenService = refTokenService;
    }

    @PostMapping("/auth/v1/signup")
    public ResponseEntity SignUp(@RequestBody SignupDTO signupDTO) {
        if(authService.isAlreadySignedUp(signupDTO.getUsername()))
            return new ResponseEntity<>("Already Present", HttpStatus.BAD_REQUEST);

        try {
            UserInfo user = authService.signup(signupDTO.getUsername(), signupDTO.getPassword());
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
            String jwtToken = jwtService.generateToken(userDetails);
            String refToken = refTokenService.createRefToken(user);

            return new ResponseEntity<>(TokenResponeDTO.builder().jwtToken(jwtToken).refreshToken(refToken).build(),HttpStatus.OK);
        }catch (Exception e){
                e.printStackTrace();
                return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
