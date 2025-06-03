package com.supermarket.promows.model.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LicenseInfoDTO {
    private LocalDateTime endDate;
    private LocalDateTime lastCheckDate;
    private String licenseKey;

    public LicenseInfoDTO(LocalDateTime endDate, LocalDateTime lastCheckDate, String licenseKey) {
        this.endDate = endDate;
        this.lastCheckDate = lastCheckDate;
        this.licenseKey = licenseKey;
    }
}
