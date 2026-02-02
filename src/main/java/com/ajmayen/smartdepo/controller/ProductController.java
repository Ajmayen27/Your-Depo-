package com.ajmayen.smartdepo.controller;

import com.ajmayen.smartdepo.dto.ProductRequest;
import com.ajmayen.smartdepo.model.Category;
import com.ajmayen.smartdepo.model.Product;
import com.ajmayen.smartdepo.repository.CategoryRepository;
import com.ajmayen.smartdepo.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService  productService;
    private final CategoryRepository categoryRepository;

    public ProductController(ProductService productService, CategoryRepository categoryRepository) {
        this.productService = productService;
        this.categoryRepository = categoryRepository;
    }

    @PostMapping
    public Product createProduct(@RequestBody ProductRequest request) {

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = Product.builder()
                .category(category)
                .name(request.getName())
                .skuCode(request.getSkuCode())
                .perCartonPieces(request.getPerCartonPieces())
                .pricePerCarton(request.getPricePerCarton())
                .build();

        return productService.createProduct(product);
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }


}
