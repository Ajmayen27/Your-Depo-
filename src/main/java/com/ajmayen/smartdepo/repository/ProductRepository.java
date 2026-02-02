package com.ajmayen.smartdepo.repository;

import com.ajmayen.smartdepo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
