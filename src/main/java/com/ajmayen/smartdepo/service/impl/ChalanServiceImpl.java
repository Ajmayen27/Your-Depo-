package com.ajmayen.smartdepo.service.impl;

import com.ajmayen.smartdepo.dto.ChalanItemRequest;
import com.ajmayen.smartdepo.dto.ChalanItemResponse;
import com.ajmayen.smartdepo.dto.ChalanRequest;
import com.ajmayen.smartdepo.dto.ChalanResponse;
import com.ajmayen.smartdepo.model.*;
import com.ajmayen.smartdepo.repository.ChalanRepository;
import com.ajmayen.smartdepo.repository.ProductRepository;
import com.ajmayen.smartdepo.repository.StockRepository;
import com.ajmayen.smartdepo.service.ChalanService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChalanServiceImpl implements ChalanService {

    private final ChalanRepository chalanRepository;
    private final ProductRepository productRepository;
    private final StockRepository stockRepository;

    public ChalanServiceImpl(ChalanRepository chalanRepository, ProductRepository productRepository, StockRepository stockRepository) {
        this.chalanRepository = chalanRepository;
        this.productRepository = productRepository;
        this.stockRepository = stockRepository;
    }

    @Override
    @Transactional
    public ChalanResponse createChalan(ChalanRequest request) {

        // Step 1: Create Chalan Object
        Chalan chalan = Chalan.builder()
                .chalanNo(request.getChalanNo())
                .chalanDate(request.getChalanDate())
                .type(request.getType())
                .subTotal(0.0)
                .items(new ArrayList<>())
                .build();

        double subTotal = 0;
        List<ChalanItemResponse> responseItems = new ArrayList<>();

        // Step 2: Process Items
        for (ChalanItemRequest itemReq : request.getItems()) {

            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            Stock stock = stockRepository.findByProductId(product.getId())
                    .orElseThrow(() -> new RuntimeException("Stock not found"));

            int qty = itemReq.getCartonQty();

            // Step 3: Stock Update Logic
            if (request.getType() == ChalanType.OUTGOING) {

                // Validation
                if (stock.getCurrentCartonQty() < qty) {
                    throw new RuntimeException(
                            "Not enough stock for product: " + product.getName()
                    );
                }

                // Deduct stock
                stock.setCurrentCartonQty(stock.getCurrentCartonQty() - qty);
            }

            if (request.getType() == ChalanType.INCOMING) {

                // Add stock
                stock.setCurrentCartonQty(stock.getCurrentCartonQty() + qty);
            }

            stockRepository.save(stock);

            // Step 4: Price Calculation
            double lineTotal = product.getPricePerCarton() * qty;
            subTotal += lineTotal;

            // Step 5: Save Item Entity
            ChalanItem item = ChalanItem.builder()
                    .chalan(chalan)
                    .product(product)
                    .cartonQty(qty)
                    .pricePerCarton(product.getPricePerCarton())
                    .totalPrice(lineTotal)
                    .build();

            chalan.getItems().add(item);

            // Step 6: Build Response Item (Includes Pieces/Carton)
            responseItems.add(
                    ChalanItemResponse.builder()
                            .productId(product.getId())
                            .productName(product.getName())
                            .perCartonPieces(product.getPerCartonPieces())
                            .pricePerCarton(product.getPricePerCarton())
                            .cartonQty(qty)
                            .totalPrice(lineTotal)
                            .build()
            );
        }

        // Step 7: Save Chalan
        chalan.setSubTotal(subTotal);
        Chalan saved = chalanRepository.save(chalan);

        // Step 8: Return Response DTO
        return ChalanResponse.builder()
                .id(saved.getId())
                .chalanNo(saved.getChalanNo())
                .chalanDate(saved.getChalanDate())
                .type(saved.getType())
                .subTotal(saved.getSubTotal())
                .items(responseItems)
                .build();
    }
}
