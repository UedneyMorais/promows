package com.supermarket.promows.exception;

public class PromotionAlreadyExistsException extends RuntimeException {
    public PromotionAlreadyExistsException(String productEan) {
        super("Já existe uma promoção com o código de barras: " + productEan + " cadastrada e ativa para este produto.");
    }
}

