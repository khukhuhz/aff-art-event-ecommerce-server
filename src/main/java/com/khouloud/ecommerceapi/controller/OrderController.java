package com.khouloud.ecommerceapi.controller;

import com.khouloud.ecommerceapi.common.Action;
import com.khouloud.ecommerceapi.common.EcommerceApiResponse;
import com.khouloud.ecommerceapi.common.OrderStatus;
import com.khouloud.ecommerceapi.dto.OrderDtoResponse;
import com.khouloud.ecommerceapi.dto.OrderItemDtoRequest;
import com.khouloud.ecommerceapi.service.OrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/e-commerce-api/v1/orders")
@AllArgsConstructor
@Slf4j
public class OrderController {

    public static final String ORDER_WAS_PLACED = "La commande a été placée";
    public static final String ORDER_WAS_UPDATED = "La commande a été mise à jour";
    public static final String ORDER_STATUS_CHANGED = "Etat de la commande modifié";
    public static final String QUANTITY_OF_ORDER_ITEM_WAS_CHANGED = "La quantité de commande a été modifiée";
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<EcommerceApiResponse> placeOrder(@Valid @RequestBody List<@Valid OrderItemDtoRequest> orderItemDtoRequests, @RequestParam("token") String token){
        this.orderService.placeOrder(orderItemDtoRequests, token);
        return new ResponseEntity<>(new EcommerceApiResponse(true, ORDER_WAS_PLACED), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<EcommerceApiResponse> addItemToPendingOrder(@Valid @RequestBody OrderItemDtoRequest orderItemDtoRequest, @RequestParam("token") String token){
        this.orderService.addItemToPendingOrder(orderItemDtoRequest, token);
        return new ResponseEntity<>(new EcommerceApiResponse(true, ORDER_WAS_UPDATED), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<OrderDtoResponse>> listAllOrder(@RequestParam("token") String token){
        List<OrderDtoResponse> orderDtoResponses = this.orderService.listAllOrder(token);
        return new ResponseEntity<>(orderDtoResponses, HttpStatus.OK);
    }

    @GetMapping("{orderId}")
    public ResponseEntity<OrderDtoResponse> getOrderById(@PathVariable("orderId") Long orderId, @RequestParam("token") String token){
        OrderDtoResponse orderDtoResponse = this.orderService.getOrderById(orderId, token);
        return new ResponseEntity<>(orderDtoResponse, HttpStatus.OK);
    }

    @PatchMapping("{orderId}")
    public ResponseEntity<EcommerceApiResponse> changeStatusOrder(@PathVariable("orderId") Long orderId, @RequestParam("orderStatus") OrderStatus orderStatus, @RequestParam("token") String token){
        this.orderService.changeStatusOrder(orderStatus, orderId, token);
        return new ResponseEntity<>(new EcommerceApiResponse(true, ORDER_STATUS_CHANGED), HttpStatus.OK);
    }

    @PutMapping("/{orderId}/orderItems")
    public ResponseEntity<EcommerceApiResponse> updateOrderItemQuantity(@RequestBody @Valid OrderItemDtoRequest orderItemDtoRequest, @PathVariable("orderId") Long orderId,
                                                            @RequestParam("token") String token, @RequestParam("action") Action action){
        this.orderService.updateOrderItemQuantity(orderItemDtoRequest, orderId, token, action);
        return new ResponseEntity<>(new EcommerceApiResponse(true, QUANTITY_OF_ORDER_ITEM_WAS_CHANGED), HttpStatus.CREATED);
    }

}
