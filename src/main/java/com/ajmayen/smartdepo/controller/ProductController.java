package com.ajmayen.smartdepo.controller;

import com.ajmayen.smartdepo.dto.ProductRequest;
import com.ajmayen.smartdepo.model.Category;
import com.ajmayen.smartdepo.model.Product;
import com.ajmayen.smartdepo.repository.CategoryRepository;
import com.ajmayen.smartdepo.repository.ProductRepository;
import com.ajmayen.smartdepo.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService  productService;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public ProductController(ProductService productService, CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.productService = productService;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
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


    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Category category = categoryRepository.findById(
                request.getCategoryId()
                )
                .orElseThrow(() -> new RuntimeException("Category not found"));

        product.setName(request.getName());
        product.setSkuCode(request.getSkuCode());
        product.setPerCartonPieces(request.getPerCartonPieces());
        product.setPricePerCarton(request.getPricePerCarton());
        product.setCategory(category);

        return productRepository.save(product);
    }


    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable Long id) {
        Product product  = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        productRepository.delete(product);

        return "Product deleted successfully";
    }




}
