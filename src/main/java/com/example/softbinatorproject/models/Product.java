package com.example.softbinatorproject.models;

import com.example.softbinatorproject.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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

    @NotEmpty
    private String name;

    private String description;

    @NotNull
    private Double price;

    private String expirationDate;

    @NotNull
    private Integer quantity;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name="bundle_id", nullable=false)
    @JsonIgnoreProperties("products")
    private Bundle bundle;

    private Long userId;

    public boolean isExpired() {
        return DateUtils.getCurrentDate().isAfter(LocalDate.parse(expirationDate));
    }

    public Double getPrice() {
        double discount =  (!this.isExpired() ? 0.2 : 0.5);
        return price * (1 - discount);
    }
}
