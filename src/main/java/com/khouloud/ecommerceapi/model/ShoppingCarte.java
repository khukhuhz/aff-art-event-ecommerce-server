package com.khouloud.ecommerceapi.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "SHOPPING_CARTE")
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"user", "product"})
@EqualsAndHashCode
@Getter
@Setter
@Builder
public class ShoppingCarte {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "SHOP_CARTE_ID")
    private Long shoppingCarteId;

    @NotNull
    @Column(name = "SHOP_CREATED_DATE")
    private LocalDateTime createdDate;

    @NotNull
    @Min(value = 0)
    @Column(name = "SHOP_QUANTITY")
    private Integer quantity;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "PROD_ID")
    Product product;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;
}
