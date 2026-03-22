package com.supermarket.promows.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.supermarket.promows.model.PriceBoardProduct;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PriceBoardProductDTO {

    private Long id;
    private String categoryLabel;
    private String description;
    private String packaging;
    private String unit;
    private BigDecimal price;
    /** URL completa para exibir no front */
    private String imageUrl;
    private Integer sortOrder;
    private boolean active;
    private LocalDateTime createdAt;

    public PriceBoardProductDTO(PriceBoardProduct entity, String resolvedImageUrl) {
        this.id = entity.getId();
        this.categoryLabel = entity.getCategoryLabel();
        this.description = entity.getDescription();
        this.packaging = entity.getPackaging();
        this.unit = entity.getUnit();
        this.price = entity.getPrice();
        this.imageUrl = resolvedImageUrl;
        this.sortOrder = entity.getSortOrder();
        this.active = entity.isActive();
        this.createdAt = entity.getCreatedAt();
    }
}
