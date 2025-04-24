package com.supermarket.promows.service;

import com.supermarket.promows.model.Promotion;
import com.supermarket.promows.repository.PromotionRepository;
import jakarta.transaction.Transactional;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromotionService {

    private final PromotionRepository promotionRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public PromotionService(PromotionRepository promotionRepository, SimpMessagingTemplate messagingTemplate) {
        this.promotionRepository = promotionRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @Transactional
    public Promotion createPromotion(Promotion promotion){
        Promotion savedPromotion = promotionRepository.save(promotion);

        messagingTemplate.convertAndSend("/topic/promotions", savedPromotion);
        return savedPromotion;
    }

    public List<Promotion> getActivePromotions(){
        List<Promotion> promotions = promotionRepository.findAll();
        return promotions;
    }

    public void broadcastPromotion(Promotion promotion){
        messagingTemplate.convertAndSend("/topic/promotions", promotion);
    }
}
