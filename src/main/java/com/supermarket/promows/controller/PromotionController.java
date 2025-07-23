package com.supermarket.promows.controller;

import com.supermarket.promows.dto.PromotionDTO;
import com.supermarket.promows.exception.PromotionNotFoundException;
import com.supermarket.promows.service.FileSystemStorageService;
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

    public PromotionController(PromotionService promotionService, FileSystemStorageService fileSystemStorageService) {
        this.promotionService = promotionService;
    }

    // @PostMapping
    // public ResponseEntity<PromotionDTO> createPromotion(@RequestPart("promotion") String promotionJson, @RequestPart("file") MultipartFile file){
    //     PromotionDTO createdPromotion = promotionService.createPromotion(promotionJson, file);
    //     return new ResponseEntity<>(createdPromotion, HttpStatus.CREATED);
    // }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PromotionDTO> createPromotion(@RequestPart("promotion") String promotionDTO, @RequestPart("file") MultipartFile file){
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

        PromotionDTO loadedPromotion = promotionService.getPromotionById(id);

        if (loadedPromotion != null) {
            return new ResponseEntity<>(loadedPromotion, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, value = "/{id}")
    public ResponseEntity<PromotionDTO> updatePromotionById(@RequestPart("promotion") String promotionDTO, @RequestPart(value = "file", required = false) MultipartFile file, @PathVariable Long id) {

        PromotionDTO updatedPromotion = promotionService.updatePromotionById(promotionDTO,file, id);

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
