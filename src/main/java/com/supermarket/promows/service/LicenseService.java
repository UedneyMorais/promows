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

    @Transactional
    public boolean isLicenseValid() {
        Optional<Parameter> parameter = parameterRepository.findById(1L);
        if (parameter.isEmpty()) return false;
    
        try {
            String licenseKey = parameter.get().getLicenseKey();
            String url = "http://192.168.77.137:9595/licenses/" + licenseKey + "/validate";

            ResponseEntity<LicenseResultDTO> response = restTemplate.getForEntity(url, LicenseResultDTO.class);
            LicenseResultDTO body = response.getBody();
            if (response.getStatusCode() == HttpStatus.OK && body != null) {
                parameterService.updateLicenseStatus(body);
                return body.isValid();
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

    public boolean setOfflineLicenseValid() {

        Parameter parameter = parameterRepository.findById(1L)
            .orElseThrow(() -> new ParameterNotFoundException(1L));

        if(parameter.getValidateNumberTries() <= 2 && parameter.getLastCheckDate().isBefore(LocalDateTime.now())) {
            parameter.setLastCheckDate(LocalDateTime.now().plusDays(3));
            parameter.setEndDate(LocalDateTime.now().plusDays(3));
            parameter.setValidateNumberTries(parameter.getValidateNumberTries() + 1);
            parameterRepository.save(parameter);
            return true;
        }

        return false;
    }
}
