package com.supermarket.promows.service;

import com.supermarket.promows.dto.PromotionDTO;
import com.supermarket.promows.model.Departament;
import com.supermarket.promows.model.Promotion;
import com.supermarket.promows.repository.DepartamentRepository;
import com.supermarket.promows.repository.PromotionRepository;
import jakarta.transaction.Transactional;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PromotionService {

    private final PromotionRepository promotionRepository;
    private final DepartamentRepository departamentRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public PromotionService(PromotionRepository promotionRepository, DepartamentRepository departamentRepository, SimpMessagingTemplate messagingTemplate) {
        this.promotionRepository = promotionRepository;
        this.departamentRepository = departamentRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @Transactional
    public Promotion createPromotion(PromotionDTO promotionDTO){

        Departament departament = departamentRepository.findById(promotionDTO.getDepartamentId())
            .orElseThrow(() -> new RuntimeException("Departamento não encontrado com ID: " + promotionDTO.getDepartamentId()));
            
            //Promotion promotion = new Promotion(promotionDTO, departament);   
            Promotion promotion = new Promotion();
            promotion.setProductName(promotionDTO.getProductName());
            promotion.setProductEan(promotionDTO.getProductEan());
            promotion.setProductDescription(promotionDTO.getProductDescription());
            promotion.setProductUnitTypeMessage(promotionDTO.getProductUnitTypeMessage());
            promotion.setOriginalPrice(promotionDTO.getOriginalPrice());
            promotion.setPromotionalPrice(promotionDTO.getPromotionalPrice());
            promotion.setExpirationDate(promotionDTO.getExpirationDate());
            promotion.setCustomerLimit(promotionDTO.getCustomerLimit());
            promotion.setImageUrl(promotionDTO.getImageUrl());
            promotion.setActive(promotionDTO.isActive());
            promotion.setCreatedAt(promotionDTO.getCreatedAt());
            promotion.setDepartament(departament);

            Promotion savedPromotion = promotionRepository.save(promotion);


        messagingTemplate.convertAndSend("/topic/promotions", savedPromotion);
        return savedPromotion;
    }

    @Transactional
    public List<Promotion> getActivePromotions(){
        List<Promotion> promotions = promotionRepository.findAll();
        List<Promotion> activePromotions = promotions.stream()
                .filter(Promotion::isActive)
                .collect(Collectors.toList());

        if (!activePromotions.isEmpty()) {
            // Envia cada promoção ativa individualmente (consistente com createPromotion)
            activePromotions.forEach(promo ->
                    messagingTemplate.convertAndSend("/topic/promotions", promo));

            // Ou alternativamente, envie a lista completa uma vez
            // messagingTemplate.convertAndSend("/topic/promotions", activePromotions);
        }

        return promotions;
    }

    public void broadcastPromotion(Promotion promotion){
        messagingTemplate.convertAndSend("/topic/promotions", promotion);
    }
}
