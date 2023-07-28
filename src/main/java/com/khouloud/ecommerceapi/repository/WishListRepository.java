package com.khouloud.ecommerceapi.repository;

import com.khouloud.ecommerceapi.model.Product;
import com.khouloud.ecommerceapi.model.User;
import com.khouloud.ecommerceapi.model.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Long> {
    List<WishList> findByUserOrderByCreatedDateDesc(User user);

    WishList findByUserAndProduct(User user, Product product);

}
