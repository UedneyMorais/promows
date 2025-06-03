package com.supermarket.promows.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.supermarket.promows.model.dto.ParameterDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Parameter {

    @Id
    private Long id = 1L;

    @Column(nullable = false)
    private String licenseKey;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private LocalDateTime startDate;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private LocalDateTime endDate;

    @Column(nullable = false)
    private String email;

    private String cpfCnpj;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private LocalDateTime lastCheckDate;
    
    // Novo campo para status cacheado da licença
    @Column(nullable = false)
    private Boolean licenseValid = true;
    
    // Novo campo para última verificação bem-sucedida
    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private LocalDateTime lastSuccessfulCheck = LocalDateTime.now();
    
    // Novo campo para controle de fallback
    @Column(nullable = false)
    private Boolean usingFallback = false;


    public Parameter() {
    }
    
    public Parameter(ParameterDTO parameterDTO) {
        this.licenseKey = parameterDTO.getLicenseKey();
        this.email = parameterDTO.getEmail();
        this.lastCheckDate = LocalDateTime.now();
        this.licenseValid = true; // Valor padrão
        this.lastSuccessfulCheck = LocalDateTime.now();
    }

    public boolean isWithinGracePeriod(int gracePeriodDays) {
        return this.lastSuccessfulCheck.isAfter(LocalDateTime.now().minusDays(gracePeriodDays));
    }
}
