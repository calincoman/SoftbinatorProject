package com.example.softbinatorproject.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String email;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "identitycard_id")
    @JsonIgnoreProperties("user")
    private IdentityCard identityCard;

    @OneToMany
    private List<Product> offeredProducts;

    public void addProduct(Product product) {
        this.offeredProducts.add(product);
    }

    public void deleteProduct(Product product) {
        this.offeredProducts.remove(product);
    }
}

