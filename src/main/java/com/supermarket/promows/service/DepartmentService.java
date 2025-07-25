package com.supermarket.promows.service;

import com.supermarket.promows.dto.DepartmentDTO;
import com.supermarket.promows.exception.DepartmentAlreadyExistsException;
import com.supermarket.promows.exception.DepartmentNotFoundException;
import com.supermarket.promows.exception.InconsistentIdException;
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
    public Department createDepartment(DepartmentDTO departmentDTO){

        Optional<Department> existingDepartments = departmentRepository.findBydepartmentName(departmentDTO.getDepartmentName());

        if (existingDepartments.isPresent()) {
            throw new DepartmentAlreadyExistsException(departmentDTO.getDepartmentName());
        }

        Department savedDepartment = departmentRepository.save(new Department(departmentDTO));
        return savedDepartment;
    }

    @Transactional
    public List<Department> getAllDepartments(){
        List<Department> departments = departmentRepository.findAll();
        return departments;
    }

    public Department getDepartmentById(Long id){
        return departmentRepository.findById(id).orElseThrow(() -> new DepartmentNotFoundException(id));
    }

    @Transactional
    public Department updateDepartmentById(DepartmentDTO departmentDTO, Long id) {

        if (!departmentDTO.getId().equals(id)) {
            throw new InconsistentIdException("ID do departamento na requisição não corresponde ao ID do path.");
        }
    
        Department existingDepartment = departmentRepository.findById(id)
            .orElseThrow(() -> new DepartmentNotFoundException(id));
        // Se necessário, envia notificação via WebSocket
        // messagingTemplate.convertAndSend("/topic/departments", updatedDepartment);
        
        existingDepartment.setDepartmentName(departmentDTO.getDepartmentName());

        return departmentRepository.save(existingDepartment);
    }

    @Transactional
    public void deleteDepartmentById(Long id) {
        Department loadedDepartment = departmentRepository.findById(id)
            .orElseThrow(() -> new DepartmentNotFoundException(id));
        
        departmentRepository.delete(loadedDepartment);
    }
}
