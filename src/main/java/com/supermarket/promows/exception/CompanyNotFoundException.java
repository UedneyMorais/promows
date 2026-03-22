package com.supermarket.promows.exception;

public class CompanyNotFoundException extends RuntimeException {

    public CompanyNotFoundException(Long id) {
        super("Empresa não encontrada: " + id);
    }
}
