package com.khouloud.ecommerceapi.repository;

import com.khouloud.ecommerceapi.common.OrderStatus;
import com.khouloud.ecommerceapi.model.Order;
import com.khouloud.ecommerceapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByUserAndOrderStatus(User user, OrderStatus pending);

    List<Order> findByUser(User user);

    Optional<Order> findByOrderIdAndUser(Long orderId, User user);

    List<Order> findByUserOrderByCreatedDateDesc(User user);
}
