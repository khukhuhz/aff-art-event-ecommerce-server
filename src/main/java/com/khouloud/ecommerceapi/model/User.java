package com.khouloud.ecommerceapi.model;

import com.khouloud.ecommerceapi.common.Role;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@Entity
@Table(name = "USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "USERS_ID")
    private Integer userId;

    @NotBlank
    @Column(name = "USERS_FIRST_NAME")
    private String firstName;

    @NotBlank
    @Column(name = "USERS_LAST_NAME")
    private String lastName;

    @NotBlank
    @Column(name = "USERS_EMAIL")
    private String email;

    @NotBlank
    @Column(name = "USERS_PASSWORD")
    private String password;

    @NotNull
    @Column(name = "USERS_ROLE")
    @Enumerated(EnumType.STRING)
    private Role role;
}
