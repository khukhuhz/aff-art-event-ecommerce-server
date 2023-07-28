package com.khouloud.ecommerceapi.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "ORDER_ITEM")
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {"order"})
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "ORDER_ITEM_ID")
    private Long orderItemId;

    @NotNull
    @Min(value = 1)
    @Column(name = "QUANTITY")
    private Long quantity;

    @NotNull
    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;

    @NotNull
    @OneToOne(targetEntity = Product.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "PROD_ID")
    private Product product;

    @NotNull
    @ManyToOne(targetEntity = Order.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "Order_ID")
    private Order order;
}
