package com.supermarket.promows.exception;

public class ParameterNotFoundException extends RuntimeException {
    public ParameterNotFoundException(Long id) {
        super("Parametro com ID " + id + " não encontrado.");
    }
}
