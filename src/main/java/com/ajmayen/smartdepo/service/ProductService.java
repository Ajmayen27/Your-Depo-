package com.ajmayen.smartdepo.service;

import com.ajmayen.smartdepo.model.Product;

import java.util.List;

public interface ProductService {

    Product createProduct(Product product);
    List<Product> getAllProducts();

}
