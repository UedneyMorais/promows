package com.supermarket.promows.service;

import com.supermarket.promows.model.Department;
import com.supermarket.promows.repository.DepartmentRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public Department createDepartment(Department department){
        Department savedDepartment = departmentRepository.save(department);
        return savedDepartment;
    }

    public List<Department> getActiveDepartments(){
        List<Department> departments = departmentRepository.findAll();
        return departments;
    }
}
