package com.ajmayen.smartdepo.service.impl;

import com.ajmayen.smartdepo.model.Product;
import com.ajmayen.smartdepo.model.Stock;
import com.ajmayen.smartdepo.repository.ProductRepository;
import com.ajmayen.smartdepo.repository.StockRepository;
import com.ajmayen.smartdepo.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final StockRepository stockRepository;

    public ProductServiceImpl(ProductRepository productRepository, StockRepository stockRepository) {
        this.productRepository = productRepository;
        this.stockRepository = stockRepository;
    }

    @Override
    public Product createProduct(Product product) {
        Product savedProduct = productRepository.save(product);

        // Auto create stock row
        Stock stock = Stock.builder()
                .product(savedProduct)
                .currentCartonQty(0)
                .build();

        stockRepository.save(stock);

        return savedProduct;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }




}
