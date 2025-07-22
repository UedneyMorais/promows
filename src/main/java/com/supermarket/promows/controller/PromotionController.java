package com.supermarket.promows.controller;

import com.supermarket.promows.dto.PromotionDTO;
import com.supermarket.promows.exception.PromotionNotFoundException;
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
    public List<Promotion> getAllPromotions() {
        List<Promotion> promotions = promotionService.getAllPromotions();
        return promotions;
    }

    @GetMapping("/valid")
    public List<Promotion> getAllValidPromotions() {
        List<Promotion> validPromotions = promotionService.getAllValidPromotions();
        return validPromotions;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Promotion> getPromotionById(@PathVariable Long id) {
        
        Promotion loadedPromotion = promotionService.getPromotionById(id);

        if (loadedPromotion != null) {
            return new ResponseEntity<>(loadedPromotion, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Promotion> updatePromotionById(@RequestBody PromotionDTO promotionDTO, @PathVariable Long id) {
        
        Promotion updatedPromotion = promotionService.updatePromotionById(promotionDTO, id);

        if (updatedPromotion != null) {
            return new ResponseEntity<>(updatedPromotion, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePromotionById(@PathVariable Long id) {
        try {
            promotionService.deletePromotionById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (PromotionNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

//    @PostMapping("/upload")
//    public ResponseEntity<String> uploadImage(@RequestParam MultipartFile file) {
//        // Salva a imagem no banco ou em um storage externo
//    }
}
