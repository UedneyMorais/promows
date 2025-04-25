package com.supermarket.promows.controller;

import com.supermarket.promows.model.Departament;
import com.supermarket.promows.service.DepartamentService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/departaments")
public class DepartamentController {
    private final DepartamentService departamentService;

    public DepartamentController(DepartamentService departamentService) {
        this.departamentService = departamentService;
 
    }

    @PostMapping
    public ResponseEntity<Departament> createDepartament(@RequestBody Departament departament){
        Departament createdDepartament = departamentService.createDepartament(departament);
        return new ResponseEntity<Departament>(createdDepartament, HttpStatus.CREATED);
    }


    @GetMapping
    public List<Departament> getActiveDepartaments() {
        List<Departament> departaments = departamentService.getActiveDepartaments();
        return departaments;
    }
}
