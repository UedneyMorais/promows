package com.supermarket.promows.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PromotionDTO {

    @NotBlank(message = "Nome do produto(productName) não pode ser vazio")
    @Size(min = 3, max = 50, message = "O nome deve ter entre 3 e 50 caracteres")
    private String productName;

    @NotBlank(message = "Código de barras do produto(productEan) não pode ser vazio")
    private String productEan;

    @NotBlank(message = "Descrição do produto(productDescription) não pode ser vazio")
    private String productDescription;

    @NotBlank(message = "Mensagem do tipo da unidade do produto(productUnitTypeMessage) não pode ser vazio")
    private String productUnitTypeMessage;

    @NotBlank(message = "Preço original(originalPrice) não pode ser vazio")
    private BigDecimal originalPrice;

    @NotBlank(message = "Preço promocional(promotionalPrice) não pode ser vazio")
    private BigDecimal promotionalPrice;

    @NotBlank(message = "Data de expiração(expirationDate) não pode ser vazio")
    private LocalDateTime expirationDate;

    @NotBlank(message = "Limite de item para clientes(customerLimit) não pode ser vazio")
    private int customerLimit;

    @NotBlank(message = "URL da imagem(imageUrl) não pode ser vazio")
    private String imageUrl;

    @NotBlank(message = "Ativo(active) não pode ser vazio")
    private boolean active;

    @NotBlank(message = "ID do departamento(departmentId) não pode ser vazio")
    private Long departmentId;
    
    private LocalDateTime createdAt;
}
