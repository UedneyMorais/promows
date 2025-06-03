package com.supermarket.promows.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepartmentDeleteDTO {
    
    private Long id;

    public DepartmentDeleteDTO(Long id) {
        this.id = id;
    }
}
