package com.supermarket.promows.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LicenseResultDTO {
    private boolean valid;
    private String message;
    private String errors;

    public LicenseResultDTO(boolean valid, String message, String errors) {
        this.valid = valid;
        this.message = message;
        this.errors = errors;
    }
}
