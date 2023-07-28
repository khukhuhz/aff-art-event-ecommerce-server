package com.khouloud.ecommerceapi.service;

import com.khouloud.ecommerceapi.common.Action;
import com.khouloud.ecommerceapi.common.OrderStatus;
import com.khouloud.ecommerceapi.dto.OrderDtoResponse;
import com.khouloud.ecommerceapi.dto.OrderItemDtoRequest;
import com.khouloud.ecommerceapi.dto.OrderItemDtoResponse;
import com.khouloud.ecommerceapi.exceptions.*;
import com.khouloud.ecommerceapi.model.Order;
import com.khouloud.ecommerceapi.model.OrderItem;
import com.khouloud.ecommerceapi.model.Product;
import com.khouloud.ecommerceapi.model.User;
import com.khouloud.ecommerceapi.repository.OrderItemRepository;
import com.khouloud.ecommerceapi.repository.OrderRepository;
import com.khouloud.ecommerceapi.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    public static final String PRODUCT_LIST_IS_EMPTY = "La liste des produits est vide";
    public static final String CAN_NOT_CREATE_A_NEW_ORDER_THERE_IS_A_PENDING_ORDER = "Impossible de créer une nouvelle commande. Il y a une commande en attente {}";
    public static final String CAN_NOT_CREATE_A_NEW_ORDER_YOU_HAVE_PENDING_ORDER_WITH_ID_S = "Impossible de créer une nouvelle commande. Commande en attente avec id: %s";
    public static final String CAN_NOT_ADD_ITEM_NO_PENDING_ORDER = "Impossible d'ajouter un article, aucune commande en attente";
    public static final String INVALID_PRODUCT_ID = "Numéro de produit non valide: %s";
    public static final String EXISTENT_QUANTITY_IS_LESS_OR_EQUAL_TO_SEND_QUANTITY = "Quantité existante: %s est inférieure ou égale à la quantité envoyer: %s";
    public static final String QUANTITY_MUST_BE_GREATER_THAN_OR_EQUAL_TO_1 = "La quantité doit être supérieure ou égale à 1";
    public static final String NO_ORDER_WITH_ID_FOR_USER_WITH_ID = "Aucune commande avec id: %s pour l'utilisateur avec id: %s";

    private AuthenticationService authenticationService;
    private ProductRepository productRepository;
    private OrderRepository orderRepository;
    private OrderItemRepository orderItemRepository;
    private ModelMapper modelMapper;

    @Override
    @Transactional
    public void placeOrder(List<OrderItemDtoRequest> orderItemDtoRequests, String token) {
        final User user = this.authenticationService.getUser(token);
        if (orderItemDtoRequests.isEmpty()) {
            throw new CustomException(PRODUCT_LIST_IS_EMPTY);
        }
        Order order = this.orderRepository.findByUserAndOrderStatus(user, OrderStatus.PENDING);
        if(Objects.nonNull(order)){
            log.info(CAN_NOT_CREATE_A_NEW_ORDER_THERE_IS_A_PENDING_ORDER, order);
            throw new PendingOrderExistsException(String.format(CAN_NOT_CREATE_A_NEW_ORDER_YOU_HAVE_PENDING_ORDER_WITH_ID_S, order.getOrderId()));
        }
        List<OrderItem> orderItems = orderItemDtoRequests.stream()//
                .map(orderItemDtoRequest -> mapOrderItemDtoToEntity(orderItemDtoRequest))//
                .collect(Collectors.toList());
        Order orderToSave = Order.builder().orderStatus(OrderStatus.PENDING).createdDate(LocalDateTime.now()).user(user).build();
        Order savedOrder = this.orderRepository.save(orderToSave);
        orderItems.stream().forEach(orderItem -> {
            orderItem.setOrder(savedOrder);
            this.orderItemRepository.save(orderItem);
        });
    }

    @Override
    public void addItemToPendingOrder(OrderItemDtoRequest orderItemDtoRequest, String token) {
        User user = this.authenticationService.getUser(token);
        Order pendingOrder = this.orderRepository.findByUserAndOrderStatus(user, OrderStatus.PENDING);
        if(Objects.isNull(pendingOrder)) {
            throw new NoPendingOrderExistsException(CAN_NOT_ADD_ITEM_NO_PENDING_ORDER);
        }
        saveItemInPendingOrder(orderItemDtoRequest, pendingOrder, Action.PLUS);
    }

    @Override
    public List<OrderDtoResponse> listAllOrder(String token) {
        User user = this.authenticationService.getUser(token);
        List<Order> orders = this.orderRepository.findByUserOrderByCreatedDateDesc(user);
        if(Objects.isNull(orders) || orders.isEmpty()){
            return new ArrayList<>();
        }
        List<OrderDtoResponse> orderDtoResponses = orders.stream().map(order -> mapOrderEntityToDtoResponse(order)).collect(Collectors.toList());
        return orderDtoResponses;
    }

    @Override
    public OrderDtoResponse getOrderById(Long orderId, String token) {
        User user = this.authenticationService.getUser(token);
        Order order = this.orderRepository.findByOrderIdAndUser(orderId, user)
                .orElseThrow(() -> getNoOrderExistsException(orderId, user));
        OrderDtoResponse orderDtoResponse = mapOrderEntityToDtoResponse(order);
        return orderDtoResponse;
    }

    @Override
    public void changeStatusOrder(OrderStatus orderStatus, Long orderId, String token) {
        User user = this.authenticationService.getUser(token);
        Order order = this.orderRepository.findByOrderIdAndUser(orderId, user).orElseThrow(() -> getNoOrderExistsException(orderId, user));
        if(!OrderStatus.PENDING.equals(order.getOrderStatus()) && OrderStatus.PENDING.equals(orderStatus)){
            throw new ChangeStatusException("Can not change order status to PENDING");
        }
        order.setOrderStatus(orderStatus);
        this.orderRepository.save(order);
    }

    @Override
    public void updateOrderItemQuantity(OrderItemDtoRequest orderItemDtoRequest, Long orderId, String token, Action action) {
        User user = this.authenticationService.getUser(token);
        Order pendingOrder = this.orderRepository.findByUserAndOrderStatus(user, OrderStatus.PENDING);
        if(Objects.isNull(pendingOrder) || pendingOrder.getOrderId() != orderId) {
            throw new NoPendingOrderExistsException(CAN_NOT_ADD_ITEM_NO_PENDING_ORDER);
        }
        saveItemInPendingOrder(orderItemDtoRequest, pendingOrder, action);
    }

    private OrderDtoResponse mapOrderEntityToDtoResponse(Order order) {
        OrderDtoResponse orderDtoResponse = this.modelMapper.map(order, OrderDtoResponse.class);

        List<OrderItemDtoResponse> orderItemDtoResponses = order.getOrderItems().stream()
                .map(orderItem -> this.modelMapper.map(orderItem, OrderItemDtoResponse.class))
                .collect(Collectors.toList());

        double totalPrice = calculateTotalPriceOfOrder(orderItemDtoResponses);
        orderDtoResponse.setOrderItemDtoResponses(orderItemDtoResponses);
        orderDtoResponse.setTotalPrice(totalPrice);
        return orderDtoResponse;
    }

    private double calculateTotalPriceOfOrder(List<OrderItemDtoResponse> orderItemDtoResponses) {
        return orderItemDtoResponses.stream()
                .mapToDouble(orderItemDtoResponse -> orderItemDtoResponse.getQuantity() * orderItemDtoResponse.getProductDtoResponse().getPrice())
                .sum();
    }

    private void saveItemInPendingOrder(OrderItemDtoRequest orderItemDtoRequest, Order pendingOrder, Action action) {

        OrderItem orderItem = findItemInPendingOrder(orderItemDtoRequest.getProductId(), pendingOrder);
        if(Objects.nonNull(orderItem.getOrderItemId())){
            saveItemWhenOrderItemExistInPendingOrder(orderItemDtoRequest, orderItem, action);
        }else{
            saveItemWhenItemNotExistInPendingOrder(orderItemDtoRequest, pendingOrder, orderItem);
        }
    }

    private void saveItemWhenItemNotExistInPendingOrder(OrderItemDtoRequest orderItemDtoRequest, Order pendingOrder, OrderItem orderItem) {
        Product product = this.productRepository.findById(orderItemDtoRequest.getProductId()).orElseThrow(() -> {
            log.error("Invalid Product id : {}", orderItemDtoRequest.getProductId());
            return new NoProductExistsException(String.format(INVALID_PRODUCT_ID, orderItemDtoRequest.getProductId()));
        });
        orderItem.setProduct(product);
        orderItem.setOrder(pendingOrder);
        orderItem.setQuantity(orderItemDtoRequest.getQuantity());
        orderItem.setCreatedDate(LocalDateTime.now());
        this.orderItemRepository.save(orderItem);
    }

    private OrderItem findItemInPendingOrder(Long productId, Order pendingOrder) {
        return pendingOrder.getOrderItems().stream()
                .filter(item -> item.getProduct().getProductId() == productId)
                .findFirst().orElseGet(OrderItem::new);
    }

    private void saveItemWhenOrderItemExistInPendingOrder(OrderItemDtoRequest orderItemDtoRequest, OrderItem orderItem, Action action) {
        long newQuantity = orderItem.getQuantity();
        if(action == Action.PLUS){
            newQuantity += orderItemDtoRequest.getQuantity();
        } else if (action == Action.MINUS) {
            newQuantity -= orderItemDtoRequest.getQuantity();
        }

        if(newQuantity <= 0){
            throw new CustomException(String.format(EXISTENT_QUANTITY_IS_LESS_OR_EQUAL_TO_SEND_QUANTITY,
                    orderItem.getQuantity(), orderItemDtoRequest.getQuantity()));
        }

        orderItem.setQuantity(newQuantity);
        this.orderItemRepository.save(orderItem);
    }

    private OrderItem mapOrderItemDtoToEntity(OrderItemDtoRequest orderItemDtoRequest) {
        if(orderItemDtoRequest.getQuantity() <= 0){
            throw new CustomException(QUANTITY_MUST_BE_GREATER_THAN_OR_EQUAL_TO_1);
        }
        OrderItem orderItem = this.modelMapper.map(orderItemDtoRequest, OrderItem.class);
        Product product = this.productRepository.findById(orderItemDtoRequest.getProductId()).orElseThrow(() -> {
            log.error("Invalid Product id : {}", orderItemDtoRequest.getProductId());
            return new NoProductExistsException(String.format(INVALID_PRODUCT_ID, orderItemDtoRequest.getProductId()));
        });

        orderItem.setProduct(product);
        orderItem.setCreatedDate(LocalDateTime.now());
        return orderItem;
    }

    private NoOrderExistsException getNoOrderExistsException(Long orderId, User user) {
        log.error("No order with id : {} for user with id : {}", orderId, user.getUserId());
        return new NoOrderExistsException(String.format(NO_ORDER_WITH_ID_FOR_USER_WITH_ID, orderId, user.getUserId()));
    }
}
