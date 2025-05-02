package com.supermarket.promows.service;

import com.supermarket.promows.exception.DepartmentNotFoundException;
import com.supermarket.promows.model.Department;
import com.supermarket.promows.repository.DepartmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Transactional
    public Department createDepartment(Department department){
        Department savedDepartment = departmentRepository.save(department);
        return savedDepartment;
    }

    @Transactional
    public List<Department> getActiveDepartments(){
        List<Department> departments = departmentRepository.findAll();
        return departments;
    }

    public Department getDepartmentById(Long id){
        Optional<Department> loadedDepartment = departmentRepository.findById(id);

        return loadedDepartment.orElseThrow(() -> new DepartmentNotFoundException(id));
    }

    @Transactional
    public Department updateDepartmentById(Department updatedDepartmentData, Long id) {

        if (!updatedDepartmentData.getId().equals(id)) {
            throw new IllegalArgumentException("ID do departamento na requisição não corresponde ao ID do path.");
        }
    
        Department existingDepartment = departmentRepository.findById(id)
            .orElseThrow(() -> new DepartmentNotFoundException(id));
    
        existingDepartment.setDepartmentName(updatedDepartmentData.getDepartmentName());

        if (existingDepartment.getDepartmentName() == null || existingDepartment.getDepartmentName().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do departamento não pode ser vazio");
        }
    
        Department updatedDepartment = departmentRepository.save(existingDepartment);
        
        // Se necessário, envia notificação via WebSocket
        // messagingTemplate.convertAndSend("/topic/departments", updatedDepartment);
        
        return updatedDepartment;
    }

    @Transactional
    public void deleteDepartmentById(Long id) {
        Department loadedDepartment = departmentRepository.findById(id)
            .orElseThrow(() -> new DepartmentNotFoundException(id));
    
       // DepartmentDeleteDTO deletedDepartment = new DepartmentDeleteDTO(loadedDepartment.getId());
        
        departmentRepository.delete(loadedDepartment);
        
        //messagingTemplate.convertAndSend("/topic/deleted-promotions", deletedDepartment);
    }
}
