package com.supermarket.promows.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PromotionDeleteDTO {
    private Long id;

    public PromotionDeleteDTO(Long id) {
        this.id = id;
    }
}