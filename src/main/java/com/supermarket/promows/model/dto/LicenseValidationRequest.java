package com.supermarket.promows.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LicenseValidationRequest { 
    private String licenseKey;

    public LicenseValidationRequest(String licenseKey) {
            this.licenseKey = licenseKey;
    }
}
