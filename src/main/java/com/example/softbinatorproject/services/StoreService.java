package com.example.softbinatorproject.services;


import com.example.softbinatorproject.dtos.RegisterStoreDto;
import com.example.softbinatorproject.dtos.RegisterUserDto;
import com.example.softbinatorproject.dtos.TokenDto;
import com.example.softbinatorproject.dtos.StoreInfoDto;
import com.example.softbinatorproject.models.Bundle;
import com.example.softbinatorproject.models.Product;
import com.example.softbinatorproject.models.Store;
import com.example.softbinatorproject.models.User;
import com.example.softbinatorproject.repositories.BundleRepository;
import com.example.softbinatorproject.repositories.StoreRepository;
import com.example.softbinatorproject.repositories.UserRepository;
import lombok.SneakyThrows;
import org.apache.commons.io.FileDeleteStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.ws.rs.BadRequestException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StoreService {
    private final StoreRepository storeRepository;
    private final KeycloakAdminService keycloakAdminService;
    private final BundleService bundleService;

    @Autowired
    public StoreService(StoreRepository storeRepository, KeycloakAdminService keycloakAdminService,
                        BundleService bundleService) {
        this.storeRepository = storeRepository;
        this.keycloakAdminService = keycloakAdminService;
        this.bundleService = bundleService;
    }

    public Store getStore(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Store does not exist"));

        return store;
    }

    public StoreInfoDto getStoreInfo(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Store does not exist"));

        return StoreInfoDto.builder()
                .name(store.getName())
                .email(store.getEmail())
                .location(store.getLocation())
                .officeNumber(store.getOfficeNumber())
                .build();
    }

    public List<Store> getStores() {
        List<Store> stores = storeRepository.findAll();
        return stores;
    }

    @SneakyThrows
    public TokenDto registerStore(RegisterStoreDto registerStoreDto) {
        if (storeRepository.existsByEmail(registerStoreDto.getEmail())) {
            throw new BadRequestException("Store with email " + registerStoreDto.getEmail() + " already exists");
        }
        if (storeRepository.existsByName(registerStoreDto.getName())) {
            throw new BadRequestException("Store with name " + registerStoreDto.getName() + " already exists");
        }

        Store newStore = Store.builder()
                .name(registerStoreDto.getName())
                .email(registerStoreDto.getEmail())
                .password(registerStoreDto.getPassword())
                .location(registerStoreDto.getLocation())
                .officeNumber(registerStoreDto.getOfficeNumber())
                .identificationCode(registerStoreDto.getIdentificationCode())
                .build();

        Long storeId = storeRepository.save(newStore).getId();
        TokenDto token = keycloakAdminService.addUserToKeycloak(storeId, registerStoreDto.getPassword(), "ROLE_STORE");

        return token;
    }

    public Store deleteStore(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Store does not exist"));

        bundleService.deleteBundlesOfStore(storeId);
        storeRepository.deleteById(storeId);
        return store;
    }

    public StoreInfoDto getStoreFromKeycloakStoreId(String keycloakStoreId) {
        Store store = storeRepository.findById(Long.parseLong(keycloakStoreId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Store does not exist"));

        return StoreInfoDto.builder()
                .name(store.getName())
                .email(store.getEmail())
                .location(store.getLocation())
                .officeNumber(store.getOfficeNumber())
                .offeredBundles(store.getOfferedBundles())
                .build();
    }

    public List<StoreInfoDto> getStoreDtos() {
        List<Store> stores = storeRepository.findAll();

        return stores.stream()
                .map(store -> StoreInfoDto.builder()
                        .name(store.getName())
                        .email(store.getEmail())
                        .location(store.getLocation())
                        .officeNumber(store.getOfficeNumber())
                        .offeredBundles(store.getOfferedBundles())
                        .build())
                .collect(Collectors.toList());
    }

    public List<Bundle> getBundlesOfStore(Long storeId) {
        Store store = this.getStore(storeId);

        if (store.getOfferedBundles().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Store does not have any bundles");
        }

        return store.getOfferedBundles();
    }
}
