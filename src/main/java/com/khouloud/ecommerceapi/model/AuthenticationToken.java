package com.khouloud.ecommerceapi.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = "user")
@EqualsAndHashCode
@Entity
@Table(name = "TOKENS")
public class AuthenticationToken {

    public AuthenticationToken(User user) {
        this.user = user;
        this.createdDate = LocalDateTime.now();
        this.token = UUID.randomUUID().toString();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "TOKENS_ID")
    private Long tokenId;

    @NotBlank
    @Column(name = "TOKENS_TOKEN")
    private String token;

    @NotNull
    @Column(name = "TOKENS_CREATED_DATE")
    private LocalDateTime createdDate;

    @NotNull
    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "USERS_ID")
    private User user;
}
