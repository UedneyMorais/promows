package com.supermarket.promows.exception;

public class LicenseNotFoundException extends RuntimeException {
    public LicenseNotFoundException(String license) {
        super("A licença fornecida não foi encontrada: " + license);
    }  
}
