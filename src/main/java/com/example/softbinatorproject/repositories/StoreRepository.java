package com.example.softbinatorproject.repositories;

import com.example.softbinatorproject.models.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    Optional<Store> findById(Long id);
    Optional<Store> findByEmail(String email);

    Boolean existsByEmail(String email);
    Boolean existsByName(String name);
}
