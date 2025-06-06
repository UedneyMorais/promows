package com.supermarket.utils;

import com.supermarket.promows.service.LicenseService;
import java.time.LocalDateTime;

public class ValidateLicense {

    private final LicenseService licenseService;

    public ValidateLicense(LicenseService licenseService) {
        this.licenseService = licenseService;
    }

    public boolean isLicenseValid() {
        return licenseService.isLicenseValid();
    }

    public static boolean isLicenseExpired(LocalDateTime endDate) {
        return endDate.isBefore(LocalDateTime.now());
    }

    public boolean validate() {
        boolean isValidated = false;

        if(onlineValidation()) {
            isValidated = true;
        } else {
            isValidated = offlineValidation();
        }

        return isValidated;
    }

    private boolean offlineValidation() {
        return licenseService.setOfflineLicenseValid();
    }

    private boolean onlineValidation() {
        return licenseService.isLicenseValid();
    }
}