package com.khouloud.ecommerceapi.repository;

import com.khouloud.ecommerceapi.model.Product;
import com.khouloud.ecommerceapi.model.ShoppingCarte;
import com.khouloud.ecommerceapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShoppingCarteRepository extends JpaRepository<ShoppingCarte, Long> {
    List<ShoppingCarte> findByUserOrderByCreatedDateDesc(User user);

    Optional<ShoppingCarte> findByUserAndProduct(User user, Product product);

    List<ShoppingCarte> findByUser(User user);
}
