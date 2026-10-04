package com.auth.App.config;

import com.auth.App.entities.UserRole;
import com.auth.App.repositories.RefTokenRepository;
import com.auth.App.repositories.UserRoleRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class RoleDataInit implements ApplicationRunner {

    private final UserRoleRepository userRoleRepository;

    RoleDataInit( UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        if(!userRoleRepository.existsByRoleName("ROLE_USER")) {
            userRoleRepository.save(new UserRole("ROLE_USER"));
        }
        if(!userRoleRepository.existsByRoleName("ROLE_ADMIN")) {
            userRoleRepository.save(new UserRole("ROLE_ADMIN"));
        }
    }
}
