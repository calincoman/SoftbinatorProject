package com.example.softbinatorproject.services;


import com.example.softbinatorproject.models.Bundle;
import com.example.softbinatorproject.models.Product;
import com.example.softbinatorproject.repositories.BundleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.ws.rs.BadRequestException;
import java.util.ArrayList;
import java.util.List;

@Service
public class BundleService {
    private BundleRepository bundleRepository;

    public BundleService(BundleRepository bundleRepository) {
        this.bundleRepository = bundleRepository;
    }

    public Bundle getBundle(Long id) {
        Bundle bundle = bundleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bundle not found"));
        return bundle;
    }

    public List<Bundle> getBundles() {
        return bundleRepository.findAll();
    }

    public Long addBundle(Bundle bundle) {

        if (bundleRepository.existsByName(bundle.getName())) {
            throw new BadRequestException("Bundle with name " + bundle.getName() + " already exists!");
        }

        Bundle addedBundle = bundleRepository.save(bundle);
        return addedBundle.getId();
    }

    public Bundle replaceBundle(Bundle newBundle, Long id) {
        return bundleRepository.findById(id)
                .map(bundle -> {
                    bundle.setName(newBundle.getName());
                    bundle.setDescription(newBundle.getDescription());
                    bundle.setProducts(new ArrayList<Product>(newBundle.getProducts()));
                    return bundleRepository.save(bundle);
                }).orElseGet(() -> {
                    return bundleRepository.save(newBundle);
                });
    }

    public Bundle deleteBundle(Long id) {
        Bundle bundle = bundleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bundle does not exist"));
        bundleRepository.deleteById(id);
        return bundle;
    }
}







