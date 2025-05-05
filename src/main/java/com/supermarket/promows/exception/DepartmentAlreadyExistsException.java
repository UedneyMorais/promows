package com.supermarket.promows.exception;

public class DepartmentAlreadyExistsException extends RuntimeException {
    public DepartmentAlreadyExistsException(String departmentName) {
        super("Já existe um departamento com o nome: " + departmentName);
    }
}
