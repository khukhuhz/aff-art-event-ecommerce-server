package com.khouloud.ecommerceapi.dto;

import com.khouloud.ecommerceapi.common.OrderStatus;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@EqualsAndHashCode
public class OrderDtoResponse {

    @NotNull
    private Long orderId;

    @NotNull
    private OrderStatus orderStatus;

    @NotNull
    private LocalDateTime createdDate;

    @NotNull
    private Double totalPrice;

    @NotNull
    private List<OrderItemDtoResponse> orderItemDtoResponses;
}
