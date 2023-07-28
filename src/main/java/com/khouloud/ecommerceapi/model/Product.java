package com.khouloud.ecommerceapi.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "PRODUCT")
@NoArgsConstructor
@ToString(exclude = "category")
@EqualsAndHashCode
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "PROD_ID")
    private Long productId;

    @NotBlank
    @Column(name = "PROD_NAME")
    private String productName;

    @Lob
    @Column(name = "PROD_IMG")
    private String image;

    @NotNull
    @DecimalMin("0.0")
    @Column(name = "PROD_PRICE")
    private Double price;

    @NotBlank
    @Column(name = "PROD_DESCRIPTION")
    private String description;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "CAT_ID")
    Category category;
}
