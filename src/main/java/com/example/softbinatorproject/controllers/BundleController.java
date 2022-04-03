package com.example.softbinatorproject.controllers;

import com.example.softbinatorproject.models.Bundle;
import com.example.softbinatorproject.services.BundleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bundles")
public class BundleController {
    private BundleService bundleService;

    public BundleController(BundleService bundleService) {
        this.bundleService = bundleService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBundle(@PathVariable Long id) {
        return new ResponseEntity<>(bundleService.getBundle(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getBundles() {
        return new ResponseEntity<>(bundleService.getBundles(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> addBundle(@RequestBody Bundle bundle) {
        return new ResponseEntity<>(bundleService.addBundle(bundle), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> replaceBundle(@RequestBody Bundle bundle, @PathVariable Long id) {
        return new ResponseEntity<>(bundleService.replaceBundle(bundle, id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBundle(@PathVariable Long id) {
        return new ResponseEntity<>(bundleService.deleteBundle(id), HttpStatus.OK);
    }
}
