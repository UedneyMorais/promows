package com.supermarket.promows.controller;

import com.supermarket.promows.model.Parameter;
import com.supermarket.promows.model.dto.ParameterDTO;
import com.supermarket.promows.service.ParameterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/parameters")
public class ParameterController {
    private final ParameterService parameterService;

    public ParameterController(ParameterService parameterService) {
        this.parameterService = parameterService;
 
    }

    @PostMapping("/create")
    public ResponseEntity<Parameter> createParameter(@RequestBody ParameterDTO parameterDTO){
        Parameter createdParameter = parameterService.createParameter(parameterDTO);
        return new ResponseEntity<>(createdParameter, HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<Parameter> getParameter() {
        Parameter parameter = parameterService.getParameter();
        return new ResponseEntity<>(parameter, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Parameter> getParameterById(@PathVariable Long id) { 
        Parameter loadedParameter = parameterService.getParameterById(id);
        return new ResponseEntity<>(loadedParameter, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Parameter> updateParameterById(@RequestBody ParameterDTO parameterDTO, @PathVariable Long id) {
        Parameter updatedParameter = parameterService.updateParameterById(parameterDTO, id);
        return new ResponseEntity<>(updatedParameter, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParameterById(@PathVariable Long id) {
            parameterService.deleteParameterById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
