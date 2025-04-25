package com.supermarket.promows.controller;

import com.supermarket.promows.dto.PromotionDTO;
import com.supermarket.promows.model.Promotion;
import com.supermarket.promows.service.PromotionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/promotions")
public class PromotionController {
    private final PromotionService promotionService;

    public PromotionController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    @PostMapping
    public ResponseEntity<Promotion> createPromotion(@RequestBody PromotionDTO promotionDTO){
        Promotion createdPromotion = promotionService.createPromotion(promotionDTO);
        return new ResponseEntity<Promotion>(createdPromotion, HttpStatus.CREATED);
    }


    @GetMapping
    public List<Promotion> getActivePromotions() {
        List<Promotion> promotions = promotionService.getActivePromotions();
        return promotions;
    }

//    @PostMapping("/upload")
//    public ResponseEntity<String> uploadImage(@RequestParam MultipartFile file) {
//        // Salva a imagem no banco ou em um storage externo
//    }
}
