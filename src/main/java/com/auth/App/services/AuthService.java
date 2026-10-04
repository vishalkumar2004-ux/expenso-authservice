package com.auth.App.services;
import com.auth.App.entities.UserInfo;
import com.auth.App.entities.UserRole;
import com.auth.App.repositories.UserRepository;
import com.auth.App.repositories.UserRoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;

    private final UserRoleRepository userRoleRepository;

    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserRoleRepository userRoleRepository){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRoleRepository = userRoleRepository;
    }

    public UserInfo signup(String username, String pass) {
        String hashedPass = passwordEncoder.encode(pass);
        String userId = UUID.randomUUID().toString();

        UserRole role = userRoleRepository.findByRoleName("ROLE_USER");
        if(role == null) throw new RuntimeException("something wrong in the db");

        UserInfo user = UserInfo.builder()
                .userId(userId)
                .username(username)
                .password(hashedPass)
                .build();

        user.getRoles().add(role);
        userRepository.save(user);
        return user;
    }

    public boolean isAlreadySignedUp(String username) {
        return userRepository.findByUsername(username) != null;
    }
}
