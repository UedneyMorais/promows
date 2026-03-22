package com.supermarket.promows.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supermarket.promows.dto.PriceBoardProductDTO;
import com.supermarket.promows.exception.JsonConverterDTOException;
import com.supermarket.promows.exception.PriceBoardProductNotFoundException;
import com.supermarket.promows.model.PriceBoardProduct;
import com.supermarket.promows.repository.PriceBoardProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class PriceBoardProductService {

    private final PriceBoardProductRepository repository;
    private final FileSystemStorageService fileSystemStorageService;
    private final ObjectMapper objectMapper;
    private final SimpMessagingTemplate messagingTemplate;

    public PriceBoardProductService(
            PriceBoardProductRepository repository,
            FileSystemStorageService fileSystemStorageService,
            ObjectMapper objectMapper,
            SimpMessagingTemplate messagingTemplate) {
        this.repository = repository;
        this.fileSystemStorageService = fileSystemStorageService;
        this.objectMapper = objectMapper;
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Lista ativa do painel (REST + WebSocket). Mesmo payload de {@code GET /api/price-products/board}.
     */
    public List<PriceBoardProductDTO> getAndSendActiveBoardProducts() {
        List<PriceBoardProductDTO> list = findAllActiveForBoard();
        messagingTemplate.convertAndSend("/topic/price-board", list);
        return list;
    }

    @Transactional
    public PriceBoardProductDTO create(String productJson, MultipartFile file) {
        PriceBoardProductDTO dto = parseJson(productJson);
        if (dto.getDescription() == null || dto.getDescription().isBlank()) {
            throw new IllegalArgumentException("Descrição do produto é obrigatória.");
        }
        if (dto.getPrice() == null) {
            throw new IllegalArgumentException("Preço é obrigatório.");
        }
        PriceBoardProduct entity = new PriceBoardProduct();
        applyDto(entity, dto);
        if (dto.getSortOrder() == null) {
            int max = repository.findAll().stream().mapToInt(PriceBoardProduct::getSortOrder).max().orElse(-1);
            entity.setSortOrder(max + 1);
        }
        if (file != null && !file.isEmpty()) {
            entity.setImageFilename(fileSystemStorageService.store(file));
        }
        PriceBoardProduct saved = repository.save(entity);
        getAndSendActiveBoardProducts();
        return toDto(saved);
    }

    @Transactional
    public PriceBoardProductDTO update(Long id, String productJson, MultipartFile file) {
        PriceBoardProductDTO dto = parseJson(productJson);
        if (dto.getDescription() == null || dto.getDescription().isBlank()) {
            throw new IllegalArgumentException("Descrição do produto é obrigatória.");
        }
        if (dto.getPrice() == null) {
            throw new IllegalArgumentException("Preço é obrigatório.");
        }
        PriceBoardProduct entity = repository.findById(id)
                .orElseThrow(() -> new PriceBoardProductNotFoundException(id));
        applyDto(entity, dto);
        if (file != null && !file.isEmpty()) {
            entity.setImageFilename(fileSystemStorageService.store(file));
        }
        PriceBoardProductDTO result = toDto(repository.save(entity));
        getAndSendActiveBoardProducts();
        return result;
    }

    @Transactional
    public void delete(Long id) {
        PriceBoardProduct entity = repository.findById(id)
                .orElseThrow(() -> new PriceBoardProductNotFoundException(id));
        repository.delete(entity);
        getAndSendActiveBoardProducts();
    }

    @Transactional
    public List<PriceBoardProductDTO> findAll() {
        return repository.findAllByOrderBySortOrderAscIdAsc().stream().map(this::toDto).toList();
    }

    @Transactional
    public List<PriceBoardProductDTO> findAllActiveForBoard() {
        return repository.findAllByActiveTrueOrderBySortOrderAscIdAsc().stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional
    public PriceBoardProductDTO findById(Long id) {
        PriceBoardProduct entity = repository.findById(id)
                .orElseThrow(() -> new PriceBoardProductNotFoundException(id));
        return toDto(entity);
    }

    private PriceBoardProductDTO parseJson(String json) {
        try {
            return objectMapper.readValue(json, PriceBoardProductDTO.class);
        } catch (IOException e) {
            throw new JsonConverterDTOException(e, PriceBoardProductDTO.class);
        }
    }

    private void applyDto(PriceBoardProduct entity, PriceBoardProductDTO dto) {
        String cat = dto.getCategoryLabel() != null && !dto.getCategoryLabel().isBlank()
                ? dto.getCategoryLabel().trim() : "OFERTAS";
        entity.setCategoryLabel(cat);
        entity.setDescription(dto.getDescription().trim());
        entity.setPackaging(dto.getPackaging() != null ? dto.getPackaging().trim() : "");
        String unit = dto.getUnit() != null && !dto.getUnit().isBlank() ? dto.getUnit().trim() : "kg";
        entity.setUnit(unit);
        entity.setPrice(dto.getPrice());
        if (dto.getSortOrder() != null) {
            entity.setSortOrder(dto.getSortOrder());
        }
        entity.setActive(dto.isActive());
    }

    private PriceBoardProductDTO toDto(PriceBoardProduct entity) {
        String url = entity.getImageFilename() != null
                ? fileSystemStorageService.getUrl(entity.getImageFilename())
                : null;
        return new PriceBoardProductDTO(entity, url);
    }
}
