package com.supermarket.promows.exception;

public class DepartmentNotFoundException extends RuntimeException {
    public DepartmentNotFoundException(Long id) {
        super("Departamento com ID " + id + " não encontrado.");
    }
}
