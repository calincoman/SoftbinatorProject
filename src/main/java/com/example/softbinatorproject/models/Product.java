package com.example.softbinatorproject.models;

import com.example.softbinatorproject.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.sql.Date;

@Entity
@Data
@AllArgsConstructor @NoArgsConstructor
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private Double price;

    private String expirationDate;

    private Integer quantity;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name="bundle_id", nullable=false)
    @JsonIgnoreProperties("products")
    private Bundle bundle;

    public boolean isExpired() {
        return DateUtils.getCurrentDate().isAfter(LocalDate.parse(expirationDate));
    }
}
