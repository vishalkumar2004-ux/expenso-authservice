package com.auth.App.repositories;

import com.auth.App.entities.UserInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserInfo, String> {

    UserInfo findByUsername( String username);

}
