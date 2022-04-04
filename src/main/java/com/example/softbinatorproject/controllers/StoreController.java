package com.example.softbinatorproject.controllers;

import com.example.softbinatorproject.dtos.RegisterStoreDto;
import com.example.softbinatorproject.dtos.RegisterUserDto;
import com.example.softbinatorproject.models.Bundle;
import com.example.softbinatorproject.services.BundleService;
import com.example.softbinatorproject.services.StoreService;
import com.example.softbinatorproject.services.UserService;
import com.example.softbinatorproject.utils.KeycloakUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/v1/stores")
public class StoreController {
    private final StoreService storeService;
    private final BundleService bundleService;

    public StoreController(StoreService storeService, BundleService bundleService) {
        this.storeService = storeService;
        this.bundleService = bundleService;
    }

    @GetMapping("/info")
    public ResponseEntity<?> getLoggedStore(Authentication authentication) {
        return new ResponseEntity<>(storeService.getStoreFromKeycloakStoreId(KeycloakUtils.getUser(authentication)),
                HttpStatus.OK);
    }

    @GetMapping("/info-all-stores")
    public ResponseEntity<?> getAllStoresDto(Authentication authentication) {
        return new ResponseEntity<>(storeService.getStoreDtos(), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getStoresInfo() {
        return new ResponseEntity<>(storeService.getStores(), HttpStatus.OK);
    }

    @GetMapping("/{id}/bundles")
    public ResponseEntity<?> getBundlesOfStore(@PathVariable Long id) {
        return new ResponseEntity<>(storeService.getBundlesOfStore(id), HttpStatus.OK);
    }

    @PostMapping("/register-store")
    public ResponseEntity<?> registerStore(@RequestBody RegisterStoreDto registerStoreDto) {
        return new ResponseEntity<>(storeService.registerStore(registerStoreDto), HttpStatus.OK);
    }

    @PostMapping("/add-bundle")
    public ResponseEntity<?> addBundle(@Valid @RequestBody Bundle bundle, Authentication authentication) {
        bundle.setStoreId(Long.parseLong(KeycloakUtils.getUser(authentication)));
        return new ResponseEntity<>(bundleService.addBundle(bundle), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStore(@PathVariable Long id) {
        return new ResponseEntity<>(storeService.deleteStore(id), HttpStatus.OK);
    }
}
