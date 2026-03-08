package com.ajmayen.smartdepo.repository;

import com.ajmayen.smartdepo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> { }
