package com.supermarket.promows.service;

import com.supermarket.promows.exception.ParameterNotFoundException;
import com.supermarket.promows.model.Parameter;
import com.supermarket.promows.model.dto.LicenseInfoDTO;
import com.supermarket.promows.model.dto.LicenseResultDTO;
import com.supermarket.promows.repository.ParameterRepository;
import jakarta.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class LicenseService {

    private final ParameterRepository parameterRepository;
    private final RestTemplate restTemplate;
    private final ParameterService parameterService;

    public LicenseService(ParameterRepository parameterRepository, RestTemplate restTemplate, ParameterService parameterService) {
        this.parameterRepository = parameterRepository;
        this.restTemplate = restTemplate;
        this.parameterService = parameterService;
    }

    // @Transactional
    // public boolean isLicenseValid() {
    //     Optional<Parameter> parameter = parameterRepository.findById(1L);

    //     if (parameter.isEmpty()) {
    //         throw new ParameterNotFoundException(1L);
    //     }

    //     String licenseKey = parameter.get().getLicenseKey();
    //     String url = "http://192.168.77.137:9595/licenses/" + licenseKey + "/validate";

    //     try {
    //         ResponseEntity<LicenseResultDTO> responseEntity = restTemplate.getForEntity(url, LicenseResultDTO.class);
    //         LicenseResultDTO body = responseEntity.getBody();
    //         if (body == null || !body.isValid()) {
    //             return false;
    //         }
    //     } catch (Exception e) {
    //         // Log the exception if necessary
    //         return false;
    //     }

    //     return true;
    // }

    @Transactional
    public boolean isLicenseValid() {
        Optional<Parameter> parameter = parameterRepository.findById(1L);
        if (parameter.isEmpty()) return true; // Fallback seguro
    
        // Tenta verificar no servidor
        try {
            String licenseKey = parameter.get().getLicenseKey();
            String url = "http://192.168.77.137:9595/licenses/" + licenseKey + "/validate";

            ResponseEntity<LicenseResultDTO> response = restTemplate.getForEntity(url, LicenseResultDTO.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                boolean isValid = response.getBody().isValid();
                parameterService.updateLicenseStatus(isValid); // Atualiza cache local
                parameterService.updateParameterLastCheckDate(LocalDateTime.now(), 1L);
                return isValid;
            }
        } catch (Exception e) {
                System.err.println("[AVISO] Falha na verificação de licença: " + e.getMessage());
            return false;
        }
    
        // Fallback: Usa o último status válido conhecido + grace period
        return parameterService.getCachedLicenseStatus() || 
           ChronoUnit.DAYS.between(parameter.get().getLastCheckDate(), LocalDateTime.now()) <= 1;
    }


    public LicenseInfoDTO getLicenseEndDate(){
        return parameterRepository.findById(1L)
            .map(p -> new LicenseInfoDTO(p.getEndDate(),p.getLastCheckDate(), p.getLicenseKey()))
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
