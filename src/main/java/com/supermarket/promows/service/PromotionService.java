package com.supermarket.promows.service;

import com.supermarket.promows.dto.PromotionDTO;
import com.supermarket.promows.dto.PromotionDeleteDTO;
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


            getAndSendValidPromotions();
        return savedPromotion;
    }

    @Transactional
    public List<Promotion> getAllPromotions(){
        return promotionRepository.findAll();
    }


    @Transactional
    public List<Promotion> getAllValidPromotions(){
        return getAndSendValidPromotions();
    }

    public List<Promotion> getAndSendValidPromotions() {
        List<Promotion> activePromotions = promotionRepository.findAll().stream()
                .filter(Promotion::isActive)
                .filter(promotion -> promotion.getExpirationDate() != null && promotion.getExpirationDate().isAfter(java.time.LocalDateTime.now()))
                .collect(Collectors.toList());

        if (!activePromotions.isEmpty()) {
            messagingTemplate.convertAndSend("/topic/promotions", activePromotions);
        }

        return activePromotions; 
    }


    public Promotion getPromotionById(Long id){
        Optional<Promotion> loadedPromotion = promotionRepository.findById(id);

        return loadedPromotion.orElseThrow(() -> new PromotionNotFoundException(id));
    }

    @Transactional
    public Promotion updatePromotionById(PromotionDTO promotionDTO, Long id) {
        Optional<Promotion> loadedPromotionOptional = promotionRepository.findById(id);

        Promotion loadedPromotion = loadedPromotionOptional.orElseThrow(() -> new PromotionNotFoundException(id));

        // if (!promotionDTO.getId().equals(id)) {
        //     throw new IllegalArgumentException("ID da promoção na requisição não corresponde ao ID do path.");
        // }

         Long departmentId = promotionDTO.getDepartmentId(); 
         Department department = departmentRepository.findById(departmentId)
                 .orElseThrow(() -> new DepartmentNotFoundException(departmentId)); 
        loadedPromotion.setProductName(promotionDTO.getProductName());
        loadedPromotion.setProductEan(promotionDTO.getProductEan());
        loadedPromotion.setProductDescription(promotionDTO.getProductDescription());
        loadedPromotion.setProductUnitTypeMessage(promotionDTO.getProductUnitTypeMessage());
        loadedPromotion.setOriginalPrice(promotionDTO.getOriginalPrice());
        loadedPromotion.setPromotionalPrice(promotionDTO.getPromotionalPrice());
        loadedPromotion.setExpirationDate(promotionDTO.getExpirationDate());
        loadedPromotion.setCustomerLimit(promotionDTO.getCustomerLimit());
        loadedPromotion.setImageUrl(promotionDTO.getImageUrl());
        loadedPromotion.setActive(promotionDTO.isActive());
        loadedPromotion.setDepartment(department);

        Promotion updatedPromotion = promotionRepository.save(loadedPromotion);
    
        // Envia a promoção atualizada via WebSocket
        //messagingTemplate.convertAndSend("/topic/promotions", updatedPromotion);

        getAndSendValidPromotions();
        
        return updatedPromotion;
    }

    @Transactional
    public void deletePromotionById(Long id) {
        Promotion loadedPromotion = promotionRepository.findById(id)
            .orElseThrow(() -> new PromotionNotFoundException(id));
        
        promotionRepository.delete(loadedPromotion);
        
        //messagingTemplate.convertAndSend("/topic/deleted-promotions", deletedPromotion);
        getAndSendValidPromotions();
    }

    // public void broadcastPromotion(Promotion promotion){
    //     //messagingTemplate.convertAndSend("/topic/promotions", promotion);
    // }
}
