package com.example.softbinatorproject.services;


import com.example.softbinatorproject.models.Bundle;
import com.example.softbinatorproject.models.Product;
import com.example.softbinatorproject.repositories.BundleRepository;
import com.example.softbinatorproject.repositories.StoreRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.ws.rs.BadRequestException;
import java.util.ArrayList;
import java.util.List;

@Service
public class BundleService {
    private final BundleRepository bundleRepository;
    private final StoreService storeService;

    public BundleService(BundleRepository bundleRepository, @Lazy StoreService storeService) {
        this.bundleRepository = bundleRepository;
        this.storeService = storeService;
    }

    public Bundle getBundle(Long id) {
        Bundle bundle = bundleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bundle does not exist"));

        return bundle;
    }

    public List<Bundle> getBundles() {
        return bundleRepository.findAll();
    }

    public Long addBundle(Bundle bundle) {
        if (bundleRepository.existsByName(bundle.getName())) {
            throw new BadRequestException("Bundle with name " + bundle.getName() + " already exists!");
        }

        storeService.getStore(bundle.getStoreId()).addBundle(bundle);
        Bundle addedBundle = bundleRepository.save(bundle);
        return addedBundle.getId();
    }

    public Bundle replaceBundle(Bundle newBundle, Long id) {
        return bundleRepository.findById(id)
                .map(bundle -> {
                    bundle.setName(newBundle.getName());
                    bundle.setDescription(newBundle.getDescription());
                    bundle.setProducts(new ArrayList<Product>(newBundle.getProducts()));
                    bundle.setStoreId(newBundle.getStoreId());
                    return bundleRepository.save(bundle);
                }).orElseGet(() -> {
                    return bundleRepository.save(newBundle);
                });
    }

    public Bundle deleteBundle(Long bundleId) {
        Bundle bundle = this.getBundle(bundleId);

        storeService.getStore(bundle.getStoreId()).deleteBundle(bundle);
        bundleRepository.deleteById(bundleId);
        return bundle;
    }

    public void deleteBundlesOfStore(Long storeId) {
        List<Bundle> bundles = storeService.getStore(storeId).getOfferedBundles();

        bundles.forEach(bundle -> {
            bundleRepository.deleteById(bundle.getId());
        });
    }
}
