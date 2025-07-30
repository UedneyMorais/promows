package com.supermarket.promows.model;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
    private int status;
    private String message;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    private String path;

    public ErrorResponse(HttpStatus status, String message, LocalDateTime timestamp, String path) {
        this.status = status.value();
        this.message = message;
        this.timestamp = timestamp;
        this.path = path;
    }
}