package com.supermarket.promows.controller;

import com.supermarket.promows.model.Promotion;
import com.supermarket.promows.service.PromotionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/promotions")
public class PromotionController {
    private final PromotionService promotionService;
    private final SimpMessagingTemplate messagingTemplate;

    public PromotionController(PromotionService promotionService, SimpMessagingTemplate messagingTemplate) {
        this.promotionService = promotionService;
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping
    public ResponseEntity<Promotion> createPromotion(@RequestBody Promotion promotion){
        Promotion createdPromotion = promotionService.createPromotion(promotion);
        return new ResponseEntity<Promotion>(createdPromotion, HttpStatus.CREATED);
    }


    @GetMapping
    public List<Promotion> getActivePromotions() {
        List<Promotion> promotions = promotionService.getActivePromotions();
        return promotions;
    }

    //TESTE REMOVER
//    @GetMapping("/trigger-ws")
//    public void triggerWebSocketTest() {
//        Promotion testPromo = new Promotion();
//        testPromo.setProductDescription("TESTE MANUAL");
//        testPromo.setDepartament("Açougue");
//        messagingTemplate.convertAndSend("/topic/promotions", testPromo);
//        System.out.println(("Mensagem TESTE MANUAL enviada para /topic/promotions"));
//    }
//    //TESTE REMOVER
//    @GetMapping("/test-ws")
//    public String testWebSocket() {
//        // Envia uma mensagem SIMPLES para teste
//        messagingTemplate.convertAndSend("/topic/promotions", "TESTE MANUAL 123");
//        return "Mensagem enviada para /topic/promotions";
//    }
//
//    @PostMapping("/upload")
//    public ResponseEntity<String> uploadImage(@RequestParam MultipartFile file) {
//        // Salva a imagem no banco ou em um storage externo
//    }
}
