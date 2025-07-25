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
    public DepartmentDTO createDepartment(DepartmentDTO departmentDTO){

        Optional<Department> existingDepartments = departmentRepository.findBydepartmentName(departmentDTO.getDepartmentName());

        if (existingDepartments.isPresent()) {
            throw new DepartmentAlreadyExistsException(departmentDTO.getDepartmentName());
        }

        Department savedDepartment = departmentRepository.save(new Department(departmentDTO));

        DepartmentDTO savedDepartmentDTO = new DepartmentDTO(savedDepartment);

        return savedDepartmentDTO;
    }

    @Transactional
    public List<DepartmentDTO> getAllDepartments(){
        List<DepartmentDTO> departments = departmentRepository.findAll().stream().map(DepartmentDTO::new).toList();
        return departments;
    }

    public DepartmentDTO getDepartmentById(Long id){
        return departmentRepository.findById(id).map(DepartmentDTO::new).orElseThrow(() -> new DepartmentNotFoundException(id));
    }

    @Transactional
    public DepartmentDTO updateDepartmentById(DepartmentDTO departmentDTO, Long id) {

        if (!departmentDTO.getId().equals(id)) {
            throw new InconsistentIdException("ID do departamento na requisição não corresponde ao ID do path.");
        }
    
        Department existingDepartment = departmentRepository.findById(id)
            .orElseThrow(() -> new DepartmentNotFoundException(id));
        // Se necessário, envia notificação via WebSocket
        // messagingTemplate.convertAndSend("/topic/departments", updatedDepartment);
        
        existingDepartment.setDepartmentName(departmentDTO.getDepartmentName());

        return new DepartmentDTO(departmentRepository.save(existingDepartment));
    }

    @Transactional
    public void deleteDepartmentById(Long id) {
        Department loadedDepartment = departmentRepository.findById(id)
            .orElseThrow(() -> new DepartmentNotFoundException(id));
        
        departmentRepository.delete(loadedDepartment);
    }
}
