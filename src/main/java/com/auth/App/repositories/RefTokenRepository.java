package com.auth.App.repositories;

import com.auth.App.entities.Token;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefTokenRepository extends CrudRepository<Token, Long> {

    Token findByRefreshToken(String token);

    Token findByUserUsername(String username);

}
