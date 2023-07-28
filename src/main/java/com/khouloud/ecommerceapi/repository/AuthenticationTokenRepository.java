package com.khouloud.ecommerceapi.repository;

import com.khouloud.ecommerceapi.model.AuthenticationToken;
import com.khouloud.ecommerceapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthenticationTokenRepository extends JpaRepository<AuthenticationToken, Long> {
    AuthenticationToken findByUser(User user);

    AuthenticationToken findByToken(String tokenStr);
}
