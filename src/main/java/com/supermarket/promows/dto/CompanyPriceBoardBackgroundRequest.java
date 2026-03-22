package com.supermarket.promows.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyPriceBoardBackgroundRequest {

    /** null ou omitido remove a seleção (usa fundo padrão do sistema nas telas) */
    private Long backgroundImageId;
}
