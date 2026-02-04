package com.ajmayen.smartdepo.repository;

import com.ajmayen.smartdepo.model.Dealer;
import com.fasterxml.jackson.annotation.JacksonAnnotation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DealerRepository extends JpaRepository<Dealer, Long> {
}
