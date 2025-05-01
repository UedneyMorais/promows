package com.supermarket.promows.dto;

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
