package com.example.softbinatorproject.repositories;

import com.example.softbinatorproject.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findById(Long id);
    Optional<Product> findByName(String name);

    Boolean existsByName(String name);
}
