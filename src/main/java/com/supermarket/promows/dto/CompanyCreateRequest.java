package com.supermarket.promows.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyCreateRequest {

    @NotBlank(message = "Nome da empresa é obrigatório.")
    private String name;

    private String address;
    private String phone;
}
