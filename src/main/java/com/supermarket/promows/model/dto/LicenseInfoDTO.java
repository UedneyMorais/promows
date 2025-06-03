package com.supermarket.promows.model.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LicenseInfoDTO {
    private LocalDateTime endDate;
    private String licenseKey;

    public LicenseInfoDTO(LocalDateTime endDate, String licenseKey) {
        this.endDate = endDate;
        this.licenseKey = licenseKey;
    }
}
