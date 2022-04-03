package com.example.softbinatorproject.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vladmihalcea.hibernate.type.json.JsonNodeStringType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Data
//@TypeDef(name = "json", typeClass = JsonNodeStringType.class)
@AllArgsConstructor @NoArgsConstructor
@Table(name = "bundle")
public class Bundle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "bundle")
    @JsonIgnoreProperties("bundle")
    private List<Product> products;

    public boolean hasExpired() {
        return products.stream()
                .anyMatch(Product::isExpired);
    }
}
