package com.supermarket.promows.model.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LicenseResultDTO {
    private boolean valid;
    private String message;
    private String licenseKey;
    private LocalDateTime endDate;
    private List<String> errors;

    public LicenseResultDTO(boolean valid, String message, String licenseKey, LocalDateTime endDate, List<String> errors) {
        this.valid = valid;
        this.message = message;
        this.licenseKey = licenseKey;
        this.endDate = endDate;
        this.errors = errors;
    }
}
