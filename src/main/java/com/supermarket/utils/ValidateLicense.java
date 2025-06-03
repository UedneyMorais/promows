package com.supermarket.utils;

import java.time.LocalDateTime;

public class ValidateLicense {
    
    public static boolean isValidLicense(String licenseKey) {
        // Implement your license validation logic here
        // For example, check if the license key matches a specific pattern or is stored in a database
        if(licenseKey == null || licenseKey.isEmpty()) {
            return false;
        }

        if(!licenseKey.equals("CDB")) {
            return false;
        }

        return true;
    }

    public static boolean isLicenseExpired(LocalDateTime endDate) {
        // Implement your logic to check if the license has expired based on the end date
        // For example, compare the end date with the current date
        return endDate.isBefore(LocalDateTime.now());
    }
}
