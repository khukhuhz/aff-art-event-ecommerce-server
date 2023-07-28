package com.khouloud.ecommerceapi.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "WISH_LIST")
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"user", "product"})
@EqualsAndHashCode
@Getter
@Setter
@Builder
public class WishList {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long wishListId;

    @NotNull
    private LocalDateTime createdDate;

    @NotNull
    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID")
    private User user;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;
}
