package com.supermarket.promows.exception;

public class PriceBoardProductNotFoundException extends RuntimeException {

    public PriceBoardProductNotFoundException(Long id) {
        super("Produto do painel de preços não encontrado: " + id);
    }
}
