package com.example.softbinatorproject.services;

import com.example.softbinatorproject.controllers.ProductController;
import com.example.softbinatorproject.models.Bundle;
import com.example.softbinatorproject.models.Product;
import com.example.softbinatorproject.models.User;
import com.example.softbinatorproject.repositories.ProductRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.ws.rs.BadRequestException;
import java.util.List;

@Service
public class ProductService {
    private ProductRepository productRepository;
    public UserService userService;

    public ProductService(ProductRepository productRepository, @Lazy UserService userService) {
        this.productRepository = productRepository;
        this.userService = userService;
    }

    public Product getProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        return product;
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public Long addProduct(Product product) {
        if (productRepository.existsByName(product.getName())) {
            throw new BadRequestException("Product with name " + product.getName() + " already exists!");
        }

        userService.getUser(product.getUserId()).addProduct(product);
        Product addedProduct = productRepository.save(product);
        return addedProduct.getId();
    }

    public Product replaceProduct(Product newProduct, Long id) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setName(newProduct.getName());
                    product.setDescription(newProduct.getDescription());
                    product.setPrice(newProduct.getPrice());
                    product.setExpirationDate(newProduct.getExpirationDate());
                    product.setQuantity(newProduct.getQuantity());
                    product.setUserId(newProduct.getUserId());
                    return productRepository.save(product);
                }).orElseGet(() -> {
                    return productRepository.save(newProduct);
                });
    }

    public Product deleteProduct(Long productId) {
        Product product = this.getProduct(productId);

        userService.getUser(product.getUserId()).deleteProduct(product);
        productRepository.deleteById(productId);
        return product;
    }

    public void deleteProductsOfUser(Long userid) {
        List<Product> products = userService.getUser(userid).getOfferedProducts();

        products.forEach(product -> {
            productRepository.deleteById(product.getId());
        });
    }
}
