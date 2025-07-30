package com.supermarket.promows.exception;
public class PromotionNotFoundException extends RuntimeException {
    public PromotionNotFoundException(Long id) {
        super("Promoção com ID " + id + " não encontrada.");
    }
}