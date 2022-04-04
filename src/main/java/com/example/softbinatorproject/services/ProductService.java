package com.example.softbinatorproject.services;

import com.example.softbinatorproject.controllers.ProductController;
import com.example.softbinatorproject.models.Product;
import com.example.softbinatorproject.models.User;
import com.example.softbinatorproject.repositories.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.ws.rs.BadRequestException;
import java.util.List;

@Service
public class ProductService {
    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
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
                    return productRepository.save(product);
                }).orElseGet(() -> {
                    return productRepository.save(newProduct);
                });
    }

    public Product deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product does not exist"));
        productRepository.deleteById(id);
        return product;
    }
}
