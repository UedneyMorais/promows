package com.supermarket.promows.exception;

import lombok.Getter;

@Getter
public class JsonConverterDTOException extends RuntimeException {

    private final Object dto;

    public JsonConverterDTOException(Throwable cause, Object dto) {
        super(cause);
        this.dto = dto;
    }  

}
