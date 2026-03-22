package com.supermarket.promows.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.supermarket.promows.dto.CompanyCreateRequest;
import com.supermarket.promows.dto.CompanyDto;
import com.supermarket.promows.dto.CompanyPriceBoardBackgroundRequest;
import com.supermarket.promows.dto.CompanyPublicDto;
import com.supermarket.promows.dto.CompanyUpdateRequest;
import com.supermarket.promows.service.CompanyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/company")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    /** Dados completos da empresa (cadastro). 204 se ainda não houver registro. */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<CompanyDto> get() {
        return companyService.getCompany()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    /** Dados públicos para telas (TV, painel): nome, logo, fundo do painel de preços, etc. */
    @GetMapping(value = "/public", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public CompanyPublicDto getPublic() {
        return companyService.getPublicProfile();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<CompanyDto> create(@Valid @RequestBody CompanyCreateRequest request) {
        return new ResponseEntity<>(companyService.create(request), HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public CompanyDto update(@PathVariable Long id, @RequestBody CompanyUpdateRequest request) {
        return companyService.update(id, request);
    }

    @PostMapping(value = "/{id}/logo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public CompanyDto uploadLogo(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        return companyService.uploadLogo(id, file);
    }

    @PostMapping(value = "/{id}/backgrounds", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public CompanyDto addBackground(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "label", required = false) String label) {
        return companyService.addBackground(id, file, label);
    }

    @DeleteMapping("/{id}/backgrounds/{backgroundId}")
    public CompanyDto deleteBackground(@PathVariable Long id, @PathVariable Long backgroundId) {
        return companyService.deleteBackground(id, backgroundId);
    }

    @PutMapping(value = "/{id}/price-board-background", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public CompanyDto setPriceBoardBackground(
            @PathVariable Long id,
            @RequestBody CompanyPriceBoardBackgroundRequest request) {
        return companyService.setPriceBoardBackground(id, request);
    }
}
