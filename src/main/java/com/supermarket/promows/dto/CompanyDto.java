package com.supermarket.promows.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CompanyDto {

    private Long id;
    private String name;
    private String address;
    private String phone;
    private String logoUrl;
    private List<CompanyBackgroundImageDto> backgroundImages = new ArrayList<>();
    private Long selectedPriceBoardBackgroundId;
}
