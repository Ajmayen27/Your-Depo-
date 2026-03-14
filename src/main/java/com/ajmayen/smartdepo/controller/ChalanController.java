package com.ajmayen.smartdepo.controller;

import com.ajmayen.smartdepo.dto.ChalanRequest;
import com.ajmayen.smartdepo.dto.ChalanResponse;
import com.ajmayen.smartdepo.repository.ChalanRepository;
import com.ajmayen.smartdepo.service.ChalanService;
import com.ajmayen.smartdepo.service.PdfGenerationService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.lowagie.text.DocumentException;

@RestController
@RequestMapping("/api/chalans")
public class ChalanController {

    private final ChalanService chalanService;
    private final PdfGenerationService pdfGenerationService;
    private final ChalanRepository chalanRepository;

    public ChalanController(ChalanService chalanService, 
                            PdfGenerationService pdfGenerationService, 
                            ChalanRepository chalanRepository) {
        this.chalanService = chalanService;
        this.pdfGenerationService = pdfGenerationService;
        this.chalanRepository = chalanRepository;
    }

    @PostMapping("/incoming")
    public ChalanResponse createIncoming(@RequestBody ChalanRequest request) {
        return chalanService.createChalan(request);
    }

    @PostMapping("/outgoing")
    public ChalanResponse createOutgoing(@RequestBody ChalanRequest request) {
        return chalanService.createChalan(request);
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> downloadChalanPdf(@PathVariable Long id) {
        return chalanRepository.findById(id).map(chalan -> {
            try {
                byte[] pdfBytes = pdfGenerationService.generateChalanPdf(chalan);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_PDF);
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=chalan_" + chalan.getChalanNo() + ".pdf");

                return ResponseEntity.ok()
                        .headers(headers)
                        .body(pdfBytes);
            } catch (DocumentException e) {
                return ResponseEntity.internalServerError().<byte[]>build();
            }
        }).orElse(ResponseEntity.notFound().build());
    }
}
