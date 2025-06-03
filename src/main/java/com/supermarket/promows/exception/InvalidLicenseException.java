package com.supermarket.promows.exception;

public class InvalidLicenseException extends RuntimeException {
    public InvalidLicenseException(String license) {
        super("A licença fornecida é inválida: " + license);
    }  
}
