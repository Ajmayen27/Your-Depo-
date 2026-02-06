package com.ajmayen.smartdepo.service.impl;

import com.ajmayen.smartdepo.dto.ChalanItemRequest;
import com.ajmayen.smartdepo.dto.ChalanItemResponse;
import com.ajmayen.smartdepo.dto.ChalanRequest;
import com.ajmayen.smartdepo.dto.ChalanResponse;
import com.ajmayen.smartdepo.model.*;
import com.ajmayen.smartdepo.repository.ChalanRepository;
import com.ajmayen.smartdepo.repository.DealerRepository;
import com.ajmayen.smartdepo.repository.ProductRepository;
import com.ajmayen.smartdepo.repository.StockRepository;
import com.ajmayen.smartdepo.service.ChalanService;
import com.ajmayen.smartdepo.service.DealerDepositService;
import com.ajmayen.smartdepo.service.OutgoingChalanService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChalanServiceImpl implements ChalanService {

    private final ChalanRepository chalanRepository;
    private final ProductRepository productRepository;
    private final DealerRepository dealerRepository;
    private final OutgoingChalanService outgoingService;
    private final StockRepository stockRepository;

    public ChalanServiceImpl(
            ChalanRepository chalanRepository,
            ProductRepository productRepository,
            DealerRepository dealerRepository,
            OutgoingChalanService outgoingService, StockRepository stockRepository) {
        this.chalanRepository = chalanRepository;
        this.productRepository = productRepository;
        this.dealerRepository = dealerRepository;
        this.outgoingService = outgoingService;
        this.stockRepository = stockRepository;
    }

    private String generateChalanNo() {
        int random = (int) (Math.random() * 100000);
        return "CH-" + random;
    }


    @Override
    @Transactional
    public ChalanResponse createChalan(ChalanRequest request) {

        Chalan chalan = Chalan.builder()
                .chalanNo(generateChalanNo())
                .chalanDate(request.getChalanDate())
                .type(request.getType())
                .items(new ArrayList<>())
                .subTotal(0.0)
                .build();

        double subTotal = 0.0;

        for (ChalanItemRequest itemReq : request.getItems()) {

            Product product = productRepository.findById(
                    itemReq.getProductId()
            ).orElseThrow(()-> new EntityNotFoundException("Product not found"));

            double lineTotal =
                    product.getPricePerCarton() * itemReq.getCartonQty();

            ChalanItem item = ChalanItem.builder()
                    .chalan(chalan)
                    .product(product)
                    .cartonQty(itemReq.getCartonQty())
                    .pricePerCarton(product.getPricePerCarton())
                    .totalPrice(lineTotal)
                    .build();

            chalan.getItems().add(item);
            subTotal += lineTotal;
        }

        chalan.setSubTotal(subTotal);

        // OUTGOING only
        if (request.getType() == ChalanType.OUTGOING) {

            if (request.getDealerId() == null) {
                throw new RuntimeException("Dealer is required for outgoing chalan");
            }

            Dealer dealer = dealerRepository
                    .findById(request.getDealerId())
                    .orElseThrow(() -> new RuntimeException("Dealer not found"));

            chalan.setDealer(dealer);

            chalan = outgoingService.processOutgoingChalan(chalan);
        }

        // ============================
        // INCOMING CHALAN
        // ============================
        else {

            for (ChalanItem item : chalan.getItems()) {

                Stock stock = stockRepository.findByProductId(
                        item.getProduct().getId()
                ).orElseThrow(() -> new RuntimeException("Stock not found"));

                stock.setCurrentCartonQty(
                        stock.getCurrentCartonQty() + item.getCartonQty()
                );

                stockRepository.save(stock);
            }

            chalan = chalanRepository.save(chalan);
        }


        return mapToResponse(chalan);
    }

    private ChalanResponse mapToResponse(Chalan chalan) {

        List<ChalanItemResponse> items = chalan.getItems().stream()
                .map(item -> ChalanItemResponse.builder()
                        .productId(item.getProduct().getId())
                        .productName(item.getProduct().getName())
                        .cartonQty(item.getCartonQty())
                        .perCartonPieces(item.getProduct().getPerCartonPieces())
                        .pricePerCarton(item.getPricePerCarton())
                        .totalPrice(item.getTotalPrice())
                        .build())
                .toList();

        return ChalanResponse.builder()
                .id(chalan.getId())
                .chalanNo(chalan.getChalanNo())
                .chalanDate(chalan.getChalanDate())
                .type(chalan.getType())
                .subTotal(chalan.getSubTotal())
                .dealerDue(chalan.getDealerDue())
                .depoDue(chalan.getDepoDue())
                .items(items)
                .build();
    }



}
