package com.khouloud.ecommerceapi.service;

import com.khouloud.ecommerceapi.common.Action;
import com.khouloud.ecommerceapi.common.OrderStatus;
import com.khouloud.ecommerceapi.dto.OrderDtoResponse;
import com.khouloud.ecommerceapi.dto.OrderItemDtoRequest;
import com.khouloud.ecommerceapi.dto.OrderItemDtoResponse;

import java.util.List;

public interface OrderService {
    void placeOrder(List<OrderItemDtoRequest> orderItemDtoRequests, String token);

    void addItemToPendingOrder(OrderItemDtoRequest orderItemDtoRequest, String token);

    List<OrderDtoResponse> listAllOrder(String token);

    OrderDtoResponse getOrderById(Long orderId, String token);

    void changeStatusOrder(OrderStatus orderStatus, Long orderId, String token);

    void updateOrderItemQuantity(OrderItemDtoRequest orderItemDtoRequest, Long orderId, String token, Action action);
}
