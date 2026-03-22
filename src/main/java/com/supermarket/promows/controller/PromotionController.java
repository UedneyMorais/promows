package com.supermarket.promows.controller;

import com.supermarket.promows.dto.PromotionDTO;
import com.supermarket.promows.exception.PromotionNotFoundException;
import com.supermarket.promows.service.PromotionService;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/api/promotions")
public class PromotionController {
    private final PromotionService promotionService;

    public PromotionController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    //@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
             produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<PromotionDTO> createPromotion(
            @RequestParam("promotion") String promotionDTO,
            @RequestParam("file") MultipartFile file) {
        PromotionDTO createdPromotion = promotionService.createPromotion(promotionDTO, file);
        return new ResponseEntity<PromotionDTO>(createdPromotion, HttpStatus.CREATED);
    }

    @GetMapping
    public List<PromotionDTO> getAllPromotions() {
        List<PromotionDTO> promotions = promotionService.getAllPromotions();
        return promotions;
    }

    @GetMapping("/valid")
    public List<PromotionDTO> getAllValidPromotions() {
        List<PromotionDTO> validPromotions = promotionService.getAllValidPromotions();
        return validPromotions;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PromotionDTO> getPromotionById(@PathVariable Long id) {
        return ResponseEntity.ok(promotionService.getPromotionById(id));
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, value = "/{id}")
    public ResponseEntity<PromotionDTO> updatePromotionById(
            @PathVariable Long id,
            @RequestParam("promotion") String promotionDTO,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        return ResponseEntity.ok(promotionService.updatePromotionById(promotionDTO, file, id));
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

}
