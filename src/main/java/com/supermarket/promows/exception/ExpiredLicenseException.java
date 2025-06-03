package com.supermarket.promows.exception;

public class ExpiredLicenseException extends RuntimeException {
    public ExpiredLicenseException(String license) {
        super("A licença fornecida está expirada: " + license);
    }  
}
