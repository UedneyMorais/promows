package com.supermarket.promows.service;

import com.supermarket.promows.dto.PromotionDTO;
import com.supermarket.promows.exception.DepartmentNotFoundException;
import com.supermarket.promows.exception.PromotionNotFoundException;
import com.supermarket.promows.model.Department;
import com.supermarket.promows.model.Promotion;
import com.supermarket.promows.repository.DepartmentRepository;
import com.supermarket.promows.repository.PromotionRepository;
import jakarta.transaction.Transactional;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PromotionService {

    private final PromotionRepository promotionRepository;
    private final DepartmentRepository departmentRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public PromotionService(PromotionRepository promotionRepository, DepartmentRepository departmentRepository, SimpMessagingTemplate messagingTemplate) {
        this.promotionRepository = promotionRepository;
        this.departmentRepository = departmentRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @Transactional
    public Promotion createPromotion(PromotionDTO promotionDTO){

        Department department = departmentRepository.findById(promotionDTO.getDepartmentId())
            .orElseThrow(() -> new RuntimeException("Departamento não encontrado com ID: " + promotionDTO.getDepartmentId()));
            
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
            promotion.setDepartment(department);

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


    public Promotion getPromotionById(Long id){
        Optional<Promotion> loadedPromotion = promotionRepository.findById(id);

        return loadedPromotion.orElseThrow(() -> new PromotionNotFoundException(id));
    }

    @Transactional
    public Promotion updatePromotionById(Promotion promotion, Long id) {
        Optional<Promotion> loadedPromotionOptional = promotionRepository.findById(id);

        Promotion loadedPromotion = loadedPromotionOptional.orElseThrow(() -> new PromotionNotFoundException(id));

        if (!promotion.getId().equals(id)) {
            throw new IllegalArgumentException("ID da promoção na requisição não corresponde ao ID do path.");
        }

         Long departmentId = loadedPromotion.getDepartment().getId(); 
         Department department = departmentRepository.findById(departmentId)
                 .orElseThrow(() -> new DepartmentNotFoundException(departmentId)); 
        loadedPromotion.setProductName(promotion.getProductName());
        loadedPromotion.setProductEan(promotion.getProductEan());
        loadedPromotion.setProductDescription(promotion.getProductDescription());
        loadedPromotion.setProductUnitTypeMessage(promotion.getProductUnitTypeMessage());
        loadedPromotion.setOriginalPrice(promotion.getOriginalPrice());
        loadedPromotion.setPromotionalPrice(promotion.getPromotionalPrice());
        loadedPromotion.setExpirationDate(promotion.getExpirationDate());
        loadedPromotion.setCustomerLimit(promotion.getCustomerLimit());
        loadedPromotion.setImageUrl(promotion.getImageUrl());
        loadedPromotion.setActive(promotion.isActive());
        loadedPromotion.setDepartment(department);

        Promotion updatedPromotion = promotionRepository.save(loadedPromotion);
    
        // Envia a promoção atualizada via WebSocket
        messagingTemplate.convertAndSend("/topic/promotions", updatedPromotion);
        
        return updatedPromotion;
    }

    public void broadcastPromotion(Promotion promotion){
        messagingTemplate.convertAndSend("/topic/promotions", promotion);
    }
}
