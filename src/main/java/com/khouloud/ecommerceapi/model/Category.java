package com.khouloud.ecommerceapi.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;


@Entity
@Table(name = "category")
@Data
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "CAT_ID")
    private Integer categoryId;

    @NotBlank
    @Column(name = "CAT_NAME")
    private String categoryName;

    @NotBlank
    @Column(name = "CAT_DESCRIPTION")
    private String description;

    @NotBlank
    @Column(name = "CAT_IMG_URL")
    private String imageURL;

}
