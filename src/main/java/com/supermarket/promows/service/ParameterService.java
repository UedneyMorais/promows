package com.supermarket.promows.service;

import com.supermarket.promows.exception.ParameterAlreadyExistsException;
import com.supermarket.promows.exception.ParameterNotFoundException;
import com.supermarket.promows.exception.InconsistentIdException;
import com.supermarket.promows.model.Parameter;
import com.supermarket.promows.model.dto.ParameterDTO;
import com.supermarket.promows.repository.ParameterRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ParameterService {

    private final ParameterRepository parameterRepository;

    public ParameterService(ParameterRepository parameterRepository) {
        this.parameterRepository = parameterRepository;
    }
    @Transactional
    public Parameter createParameter(ParameterDTO parameterDTO) {

        Optional<Parameter> existingParameters = parameterRepository.findById(1L);

        if (existingParameters.isPresent()) {
            throw new ParameterAlreadyExistsException(existingParameters.get().getCpfCnpj());
        }

        Parameter newParameter = new Parameter();
        newParameter.setId(1L);
        newParameter.setLicenseKey(parameterDTO.getLicenseKey());
        newParameter.setEmail(parameterDTO.getEmail());
        newParameter.setCpfCnpj(parameterDTO.getCpfCnpj());
        newParameter.setStartDate(LocalDateTime.now());
        newParameter.setEndDate(LocalDateTime.now());
        newParameter.setLastCheckDate(LocalDateTime.now());
        newParameter.setPhoneNumber(parameterDTO.getPhoneNumber());

        Parameter savedParameter = parameterRepository.save(newParameter);
        return savedParameter;
    }

    @Transactional
    public Parameter getParameter() {
        return parameterRepository.findById(1L).orElseThrow(() -> new ParameterNotFoundException(1L));
    }

    public Parameter getParameterById(Long id) {
        return parameterRepository.findById(id).orElseThrow(() -> new ParameterNotFoundException(id));
    }

    @Transactional
    public Parameter updateParameterById(ParameterDTO parameterDTO, Long id) {

        if (!parameterDTO.getId().equals(id)) {
            throw new InconsistentIdException("ID do departamento na requisição não corresponde ao ID do path.");
        }
    
        Parameter existingParameter = parameterRepository.findById(id)
            .orElseThrow(() -> new ParameterNotFoundException(id));
        // Se necessário, envia notificação via WebSocket
        // messagingTemplate.convertAndSend("/topic/parameters", updatedParameter);
        
        existingParameter.setCpfCnpj(parameterDTO.getCpfCnpj());
        existingParameter.setEmail(parameterDTO.getEmail());
        existingParameter.setLicenseKey(parameterDTO.getLicenseKey());
        existingParameter.setPhoneNumber(parameterDTO.getPhoneNumber());

        return parameterRepository.save(existingParameter);
    }

    @Transactional
    public void updateParameterLastCheckDate(LocalDateTime lastCheckDate, Long id) {
        Parameter parameter = parameterRepository.findById(id)
            .orElseThrow(() -> new ParameterNotFoundException(id));
        
        parameter.setLastCheckDate(lastCheckDate);
        parameterRepository.save(parameter);
    }

    

    @Transactional
    public void updateLicenseStatus(boolean isValid) {
        parameterRepository.findById(1L).ifPresent(parameter -> {
            parameter.setLicenseValid(isValid);
            parameter.setLastCheckDate(LocalDateTime.now());
            
            // Atualiza apenas se foi uma verificação bem-sucedida
            if (isValid) {
                parameter.setLastSuccessfulCheck(LocalDateTime.now());
                parameter.setUsingFallback(false);
            } else {
                parameter.setUsingFallback(true);
            }
            
            parameterRepository.save(parameter);
        });
    }
    
    @Transactional
    public boolean getCachedLicenseStatus() {
        return parameterRepository.findById(1L)
            .map(Parameter::getLicenseValid)
            .orElse(true); // Fallback seguro
    }

    @Transactional
    public LocalDateTime getLastCheckDate() {
        return parameterRepository.findById(1L)
            .map(Parameter::getLastCheckDate)
            .orElse(LocalDateTime.MIN);
    }

    @Transactional
    public void deleteParameterById(Long id) {
        Parameter loadedParameter = parameterRepository.findById(id)
            .orElseThrow(() -> new ParameterNotFoundException(id));
        
        parameterRepository.delete(loadedParameter);
        
        //messagingTemplate.convertAndSend("/topic/deleted-departaments", deletedParameter);
    }
}
