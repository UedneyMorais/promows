package com.supermarket.promows.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParameterDTO {
    private Long id;

    @NotBlank(message = "A chave de licença não pode ser vazia")
    private String licenseKey;

    @NotBlank(message = "O e-mail não pode ser vazio")
    private String email;

    @NotBlank(message = "O CPF/CNPJ não pode ser vazio")
    private String cpfCnpj;

    @NotBlank(message = "O número de telefone não pode ser vazio")
    private String phoneNumber;
}
