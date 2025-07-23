package com.supermarket.promows.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supermarket.promows.dto.DepartmentDTO;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PromotionService {

    private final PromotionRepository promotionRepository;
    private final DepartmentRepository departmentRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final FileSystemStorageService fileSystemStorageService;

    public PromotionService(PromotionRepository promotionRepository, DepartmentRepository departmentRepository, SimpMessagingTemplate messagingTemplate, FileSystemStorageService fileSystemStorageService) {
        this.promotionRepository = promotionRepository;
        this.departmentRepository = departmentRepository;
        this.messagingTemplate = messagingTemplate;
        this.fileSystemStorageService = fileSystemStorageService;
    }

    @Transactional
    public PromotionDTO createPromotion(String promotionJson, MultipartFile file) {

        // Parse JSON manualmente
        PromotionDTO promotionDTO;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            promotionDTO = objectMapper.readValue(promotionJson, PromotionDTO.class);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao converter JSON para PromotionDTO", e);
        }

        Department department = departmentRepository.findById(promotionDTO.getDepartmentId())
            .orElseThrow(() -> new RuntimeException("Departamento não encontrado com ID: " + promotionDTO.getDepartmentId()));

        String imageUrl = fileSystemStorageService.store(file);

        Promotion promotion = new Promotion();
            promotion.setProductName(promotionDTO.getProductName());
            promotion.setProductEan(promotionDTO.getProductEan());
            promotion.setProductDescription(promotionDTO.getProductDescription());
            promotion.setProductUnitTypeMessage(promotionDTO.getProductUnitTypeMessage());
            promotion.setOriginalPrice(promotionDTO.getOriginalPrice());
            promotion.setPromotionalPrice(promotionDTO.getPromotionalPrice());
            promotion.setExpirationDate(promotionDTO.getExpirationDate());
            promotion.setCustomerLimit(promotionDTO.getCustomerLimit());
            promotion.setImageUrl(imageUrl);
            promotion.setActive(promotionDTO.isActive());
            promotion.setCreatedAt(promotionDTO.getCreatedAt());
            promotion.setDepartment(department);

            Promotion createdPromotion = promotionRepository.save(promotion);

            PromotionDTO createdPromotionDTO = new PromotionDTO(createdPromotion);

            getAndSendValidPromotions();
        return createdPromotionDTO;
    }

    @Transactional
    public List<PromotionDTO> getAllPromotions() {
        List<PromotionDTO> loadedPromotionsDTO = promotionRepository.findAll().stream()
           .map(promotion -> {
               PromotionDTO dto = new PromotionDTO();
               dto.setId(promotion.getId());
               dto.setProductName(promotion.getProductName());
               dto.setProductEan(promotion.getProductEan());
               dto.setProductDescription(promotion.getProductDescription());
               dto.setProductUnitTypeMessage(promotion.getProductUnitTypeMessage());
               dto.setOriginalPrice(promotion.getOriginalPrice());
               dto.setPromotionalPrice(promotion.getPromotionalPrice());
               dto.setExpirationDate(promotion.getExpirationDate());
               dto.setCustomerLimit(promotion.getCustomerLimit());
               dto.setImageUrl(fileSystemStorageService.getUrl(promotion.getImageUrl()));
               dto.setActive(promotion.isActive());
               dto.setDepartmentId(promotion.getDepartment().getId());
               dto.setCreatedAt(promotion.getCreatedAt());
            
               // Mapear DepartmentDTO
               DepartmentDTO departmentDTO = new DepartmentDTO();
               departmentDTO.setId(promotion.getDepartment().getId());
               departmentDTO.setDepartmentName(promotion.getDepartment().getDepartmentName());
               dto.setDepartment(departmentDTO);
            
               return dto;
           })
           .collect(Collectors.toList());

        return loadedPromotionsDTO;
            
    }


    @Transactional
    public List<PromotionDTO> getAllValidPromotions(){
        return getAndSendValidPromotions();
    }

    public List<PromotionDTO> getAndSendValidPromotions() {
        List<Promotion> loadedPromotions = promotionRepository.findAll().stream()
                .filter(Promotion::isActive)
                .filter(promotion -> promotion.getExpirationDate() != null && promotion.getExpirationDate().isAfter(java.time.LocalDateTime.now()))
                .collect(Collectors.toList());


        List<PromotionDTO> promotionsToSend = loadedPromotions.stream()
           .map(promotion -> {
               PromotionDTO dto = new PromotionDTO();
               dto.setId(promotion.getId());
               dto.setProductName(promotion.getProductName());
               dto.setProductEan(promotion.getProductEan());
               dto.setProductDescription(promotion.getProductDescription());
               dto.setProductUnitTypeMessage(promotion.getProductUnitTypeMessage());
               dto.setOriginalPrice(promotion.getOriginalPrice());
               dto.setPromotionalPrice(promotion.getPromotionalPrice());
               dto.setExpirationDate(promotion.getExpirationDate());
               dto.setCustomerLimit(promotion.getCustomerLimit());
               dto.setImageUrl(fileSystemStorageService.getUrl(promotion.getImageUrl()));
               dto.setActive(promotion.isActive());
               dto.setDepartmentId(promotion.getDepartment().getId());
               dto.setCreatedAt(promotion.getCreatedAt());
            
               // Mapear DepartmentDTO
               DepartmentDTO departmentDTO = new DepartmentDTO();
               departmentDTO.setId(promotion.getDepartment().getId());
               departmentDTO.setDepartmentName(promotion.getDepartment().getDepartmentName());
               dto.setDepartment(departmentDTO);
            
               return dto;
           })
           .collect(Collectors.toList());
        
            if (!promotionsToSend.isEmpty()) {
                messagingTemplate.convertAndSend("/topic/promotions", promotionsToSend );
            }

        return promotionsToSend; 
    }


    public PromotionDTO getPromotionById(Long id){

        Optional<PromotionDTO> loadedPromotion = promotionRepository.findById(id).map(promotion -> {
            PromotionDTO dto = new PromotionDTO();
            dto.setId(promotion.getId());
            dto.setProductName(promotion.getProductName());
            dto.setProductEan(promotion.getProductEan());
            dto.setProductDescription(promotion.getProductDescription());
            dto.setProductUnitTypeMessage(promotion.getProductUnitTypeMessage());
            dto.setOriginalPrice(promotion.getOriginalPrice());
            dto.setPromotionalPrice(promotion.getPromotionalPrice());
            dto.setExpirationDate(promotion.getExpirationDate());
            dto.setCustomerLimit(promotion.getCustomerLimit());
            dto.setImageUrl(fileSystemStorageService.getUrl(promotion.getImageUrl()));
            dto.setActive(promotion.isActive());
            dto.setDepartmentId(promotion.getDepartment().getId());
            dto.setCreatedAt(promotion.getCreatedAt());

            // Mapear DepartmentDTO
            DepartmentDTO departmentDTO = new DepartmentDTO();
            departmentDTO.setId(promotion.getDepartment().getId());
            departmentDTO.setDepartmentName(promotion.getDepartment().getDepartmentName());
            dto.setDepartment(departmentDTO);

            return dto;
        });

        return loadedPromotion.orElseThrow(() -> new PromotionNotFoundException(id));
    }

    @Transactional
    public PromotionDTO updatePromotionById(String promotionJson, MultipartFile file, Long id) {

        // Parse JSON manualmente
        PromotionDTO promotionDTO;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            promotionDTO = objectMapper.readValue(promotionJson, PromotionDTO.class);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao converter JSON para PromotionDTO", e);
        }

        Optional<Promotion> loadedPromotionOptional = promotionRepository.findById(id);

        Promotion loadedPromotion = loadedPromotionOptional.orElseThrow(() -> new PromotionNotFoundException(id));

        // if (!promotionDTO.getId().equals(id)) {
        //     throw new IllegalArgumentException("ID da promoção na requisição não corresponde ao ID do path.");
        // }

        Long departmentId = promotionDTO.getDepartmentId(); 
        Department department = departmentRepository.findById(departmentId)
                 .orElseThrow(() -> new DepartmentNotFoundException(departmentId)); 

        if(file != null && !file.isEmpty()){
            String imageUrl = fileSystemStorageService.store(file);
            loadedPromotion.setImageUrl(imageUrl);
        }

        loadedPromotion.setProductName(promotionDTO.getProductName());
        loadedPromotion.setProductEan(promotionDTO.getProductEan());
        loadedPromotion.setProductDescription(promotionDTO.getProductDescription());
        loadedPromotion.setProductUnitTypeMessage(promotionDTO.getProductUnitTypeMessage());
        loadedPromotion.setOriginalPrice(promotionDTO.getOriginalPrice());
        loadedPromotion.setPromotionalPrice(promotionDTO.getPromotionalPrice());
        loadedPromotion.setExpirationDate(promotionDTO.getExpirationDate());
        loadedPromotion.setCustomerLimit(promotionDTO.getCustomerLimit());
        loadedPromotion.setImageUrl(loadedPromotion.getImageUrl());
        loadedPromotion.setActive(promotionDTO.isActive());
        loadedPromotion.setDepartment(department);

        Promotion savedPromotion = promotionRepository.save(loadedPromotion);

        PromotionDTO updatedPromotion = new PromotionDTO();
        updatedPromotion.setId(savedPromotion.getId());
        updatedPromotion.setProductName(savedPromotion.getProductName());
        updatedPromotion.setProductEan(savedPromotion.getProductEan());
        updatedPromotion.setProductDescription(savedPromotion.getProductDescription());
        updatedPromotion.setProductUnitTypeMessage(savedPromotion.getProductUnitTypeMessage());
        updatedPromotion.setOriginalPrice(savedPromotion.getOriginalPrice());
        updatedPromotion.setPromotionalPrice(savedPromotion.getPromotionalPrice());
        updatedPromotion.setExpirationDate(savedPromotion.getExpirationDate());
        updatedPromotion.setCustomerLimit(savedPromotion.getCustomerLimit());
        updatedPromotion.setImageUrl(savedPromotion.getImageUrl());
        updatedPromotion.setActive(savedPromotion.isActive());
        updatedPromotion.setDepartmentId(savedPromotion.getDepartment().getId());
        updatedPromotion.setCreatedAt(savedPromotion.getCreatedAt());

        // Mapear DepartmentDTO
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setId(savedPromotion.getDepartment().getId());
        departmentDTO.setDepartmentName(savedPromotion.getDepartment().getDepartmentName());
        updatedPromotion.setDepartment(departmentDTO);

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
