package com.supermarket.promows.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class PromotionDTO {

    private String productName;
    private String productEan;
    private String productDescription;
    private String productUnitTypeMessage;
    private BigDecimal originalPrice;
    private BigDecimal promotionalPrice;
    private LocalDateTime expirationDate;
    private int customerLimit;
    private String imageUrl;
    private boolean active;
    private Long departamentId;
    private LocalDateTime createdAt;
}
