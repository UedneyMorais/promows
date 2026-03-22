package com.supermarket.promows.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyPublicDto {

    private String companyName;
    private String address;
    private String phone;
    private String logoUrl;
    private String priceBoardBackgroundUrl;

    public static CompanyPublicDto empty() {
        return new CompanyPublicDto(null, null, null, null, null);
    }
}
