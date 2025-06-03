package com.supermarket.promows.service;

import com.supermarket.promows.exception.ParameterNotFoundException;
import com.supermarket.promows.model.Parameter;
import com.supermarket.promows.model.dto.LicenseInfoDTO;
import com.supermarket.promows.model.dto.LicenseResultDTO;
import com.supermarket.promows.repository.ParameterRepository;
import jakarta.transaction.Transactional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class LicenseService {

    private final ParameterRepository parameterRepository;
    private final RestTemplate restTemplate;

    public LicenseService(ParameterRepository parameterRepository, RestTemplate restTemplate) {
        this.parameterRepository = parameterRepository;
        this.restTemplate = restTemplate;
    }

    @Transactional
    public boolean isLicenseValid() {
        Optional<Parameter> parameter = parameterRepository.findById(1L);

        if (parameter.isEmpty()) {
            throw new ParameterNotFoundException(1L);
        }

        String licenseKey = parameter.get().getLicenseKey();
        String url = "http://192.168.77.137:9595/licenses/" + licenseKey + "/validate";

        try {
            ResponseEntity<LicenseResultDTO> responseEntity = restTemplate.getForEntity(url, LicenseResultDTO.class);
            LicenseResultDTO body = responseEntity.getBody();
            if (body == null || !body.isValid()) {
                return false;
            }
        } catch (Exception e) {
            // Log the exception if necessary
            return false;
        }

        return true;
    }

    public LicenseInfoDTO getLicenseEndDate(){
        return parameterRepository.findById(1L)
            .map(p -> new LicenseInfoDTO(p.getEndDate(), p.getLicenseKey()))
                .orElseThrow(() -> new ParameterNotFoundException(1L));
    }

    // public Department getDepartmentById(Long id){
    //     return departmentRepository.findById(id).orElseThrow(() -> new DepartmentNotFoundException(id));
    // }

    // @Transactional
    // public Department updateDepartmentById(DepartmentDTO departmentDTO, Long id) {

    //     if (!departmentDTO.getId().equals(id)) {
    //         throw new InconsistentIdException("ID do departamento na requisição não corresponde ao ID do path.");
    //     }
    
    //     Department existingDepartment = departmentRepository.findById(id)
    //         .orElseThrow(() -> new DepartmentNotFoundException(id));
    //     // Se necessário, envia notificação via WebSocket
    //     // messagingTemplate.convertAndSend("/topic/departments", updatedDepartment);
        
    //     existingDepartment.setDepartmentName(departmentDTO.getDepartmentName());

    //     return departmentRepository.save(existingDepartment);
    // }

    // @Transactional
    // public void deleteDepartmentById(Long id) {
    //     Department loadedDepartment = departmentRepository.findById(id)
    //         .orElseThrow(() -> new DepartmentNotFoundException(id));
        
    //     departmentRepository.delete(loadedDepartment);
        
    //     //messagingTemplate.convertAndSend("/topic/deleted-departaments", deletedDepartment);
    // }
}
