package com.supermarket.promows.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParameterDeleteDTO {
    
    private Long id;

    public ParameterDeleteDTO(Long id) {
        this.id = id;
    }
}
