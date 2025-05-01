package com.supermarket.promows.controller;

import com.supermarket.promows.model.Department;
import com.supermarket.promows.service.DepartmentService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {
    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
 
    }

    @PostMapping
    public ResponseEntity<Department> createDepartment(@RequestBody Department department){
        Department createdDepartment = departmentService.createDepartment(department);
        return new ResponseEntity<Department>(createdDepartment, HttpStatus.CREATED);
    }


    @GetMapping
    public List<Department> getActiveDepartments() {
        List<Department> departments = departmentService.getActiveDepartments();
        return departments;
    }
}
