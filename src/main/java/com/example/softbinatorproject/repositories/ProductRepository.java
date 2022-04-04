package com.example.softbinatorproject.repositories;

import com.example.softbinatorproject.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findById(Long id);
    Optional<Product> findByName(String name);
    Optional<List<Product>> findAllByUserId(Long userId);

    Boolean existsByName(String name);
}
