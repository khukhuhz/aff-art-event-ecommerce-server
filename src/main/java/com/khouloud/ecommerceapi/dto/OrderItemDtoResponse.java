package com.khouloud.ecommerceapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode
public class OrderItemDtoResponse {

    @NotNull
    private Long orderItemId;

    @NotNull
    private Integer quantity;

    @NotNull
    private LocalDateTime createdDate;

    @NotNull
    private Long orderId;

    @NotNull
    ProductDtoResponse productDtoResponse;
}
