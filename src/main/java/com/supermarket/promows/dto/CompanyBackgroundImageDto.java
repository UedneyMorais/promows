package com.supermarket.promows.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyBackgroundImageDto {

    private Long id;
    private String label;
    private int sortOrder;
    private String url;
}
