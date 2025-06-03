package com.supermarket.promows.exception;

public class ParameterAlreadyExistsException extends RuntimeException {
    public ParameterAlreadyExistsException(String cpfCnpj) {
        super("Já existe um parâmetro para o CPF/CNPJ: " + cpfCnpj);
    }
}
