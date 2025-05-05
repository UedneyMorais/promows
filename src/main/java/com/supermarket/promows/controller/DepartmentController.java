package com.supermarket.promows.controller;

import com.supermarket.promows.dto.DepartmentDTO;
import com.supermarket.promows.exception.DepartmentNotFoundException;
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
    public ResponseEntity<Department> createDepartment(@RequestBody DepartmentDTO departmentDTO){
        Department createdDepartment = departmentService.createDepartment(departmentDTO);
        return new ResponseEntity<Department>(createdDepartment, HttpStatus.CREATED);
    }


    @GetMapping
    public List<Department> getActiveDepartments() {
        List<Department> departments = departmentService.getActiveDepartments();
        return departments;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Department> getDepartmentById(@PathVariable Long id) {
        
        Department loadedDepartment = departmentService.getDepartmentById(id);

        if (loadedDepartment != null) {
            return new ResponseEntity<>(loadedDepartment, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Department> updateDepartmentById(@RequestBody DepartmentDTO departmentDTO, @PathVariable Long id) {
        
        Department updatedDepartment = departmentService.updateDepartmentById(departmentDTO, id);

        if (updatedDepartment != null) {
            return new ResponseEntity<>(updatedDepartment, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartmentById(@PathVariable Long id) {
        try {
            departmentService.deleteDepartmentById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (DepartmentNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
