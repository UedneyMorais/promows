package com.supermarket.promows.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "price_board_products")
@Data
@EntityListeners(AuditingEntityListener.class)
public class PriceBoardProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Ex.: BOVINOS, SUÍNOS — título da faixa vermelha */
    @Column(nullable = false, length = 80)
    private String categoryLabel;

    /** Nome/descrição do produto (ex.: CONTRA FILE) */
    @Column(nullable = false, length = 200)
    private String description;

    /** Embalagem opcional (ex.: bandeja) */
    @Column(length = 80)
    private String packaging;

    /** Unidade: kg, un, etc. */
    @Column(nullable = false, length = 20)
    private String unit;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    /** Nome do arquivo em uploads/ (sem URL completa) */
    @Column(length = 500)
    private String imageFilename;

    @Column(nullable = false)
    private int sortOrder = 0;

    @Column(nullable = false)
    private boolean active = true;

    @CreatedDate
    private LocalDateTime createdAt;
}
