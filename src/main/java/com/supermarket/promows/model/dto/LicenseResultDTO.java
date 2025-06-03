package com.supermarket.promows.model.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LicenseResultDTO {
    private boolean valid;
    private String message;
   private List<String> errors;

    public LicenseResultDTO(boolean valid, String message, List<String> errors) {
        this.valid = valid;
        this.message = message;
        this.errors = errors;
    }
}
