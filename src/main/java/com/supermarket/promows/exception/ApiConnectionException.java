package com.supermarket.promows.exception;

public class ApiConnectionException extends RuntimeException {
    public ApiConnectionException() {
        super("Erro de conexão com a API para a licença: verifique sua conexão com a internet ou o serviço de validação de licenças.");
    }  
}