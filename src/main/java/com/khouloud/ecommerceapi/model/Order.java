package com.khouloud.ecommerceapi.model;

import com.khouloud.ecommerceapi.common.OrderStatus;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "ORDERS")
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"orderItems"})
@Getter
@Setter
@EqualsAndHashCode
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "ORDER_ID")
    private Long orderId;

    @NotNull
    @Column(name = "ORDER_STATUS")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @NotNull
    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;

    @NotNull
    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID")
    private User user;

    @OneToMany(mappedBy = "order", targetEntity = OrderItem.class, fetch = FetchType.LAZY)
    private List<OrderItem>orderItems;


}
