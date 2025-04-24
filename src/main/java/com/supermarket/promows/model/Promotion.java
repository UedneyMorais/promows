package com.supermarket.promows.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String productName;
    @Column(nullable = false)
    private String productEan;
    @Column(nullable = false)
    private String productDescription;
    @Column(nullable = false)
    private String productUnitTypeMessage;
    @Column(nullable = false)
    private BigDecimal originalPrice;
    @Column(nullable = false)
    private BigDecimal promotionalPrice;
    private LocalDateTime expirationDate;
    @Column(nullable = false)
    private int customerLimit;

//    @Lob
//    @Column(columnDefinition = "BYTEA")
//    private byte[] productImage;

    private String imageUrl;
    private boolean active;
    private String departament;
    private LocalDateTime createdAt;

}
