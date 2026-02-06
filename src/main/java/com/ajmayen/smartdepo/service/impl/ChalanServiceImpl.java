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

    public ChalanServiceImpl(
            ChalanRepository chalanRepository,
            ProductRepository productRepository,
            DealerRepository dealerRepository,
            OutgoingChalanService outgoingService) {
        this.chalanRepository = chalanRepository;
        this.productRepository = productRepository;
        this.dealerRepository = dealerRepository;
        this.outgoingService = outgoingService;
    }

    @Override
    @Transactional
    public ChalanResponse createChalan(ChalanRequest request) {

        Chalan chalan = Chalan.builder()
                .chalanNo(request.getChalanNo())
                .chalanDate(request.getChalanDate())
                .type(request.getType())
                .items(new ArrayList<>())
                .subTotal(0.0)
                .build();

        double subTotal = 0;

        for (ChalanItemRequest itemReq : request.getItems()) {

            Product product = productRepository.findById(
                    itemReq.getProductId()
            ).orElseThrow();

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

            Dealer dealer = dealerRepository
                    .findById(request.getDealerId())
                    .orElseThrow();

            chalan.setDealer(dealer);

            chalan = outgoingService.processOutgoingChalan(chalan);

        } else {
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
