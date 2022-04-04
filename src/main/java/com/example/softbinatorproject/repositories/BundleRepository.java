package com.example.softbinatorproject.repositories;

import com.example.softbinatorproject.models.Bundle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BundleRepository extends JpaRepository<Bundle, Long> {

    Optional<Bundle> findById(Long id);
    Optional<Bundle > findByName(String name);
    Optional<List<Bundle>> findAllByStoreId(Long storeId);

    Boolean existsByName(String name);

}
