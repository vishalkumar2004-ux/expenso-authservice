package com.auth.App.repositories;

import com.auth.App.entities.UserRole;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends CrudRepository<UserRole, Integer> {

    UserRole findByRoleName( String roleName);

    boolean existsByRoleName(String roleName);
}
