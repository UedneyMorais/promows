package com.supermarket.promows.service;

import com.supermarket.promows.model.Departament;
import com.supermarket.promows.repository.DepartamentRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DepartamentService {

    private final DepartamentRepository departamentRepository;

    public DepartamentService(DepartamentRepository departamentRepository) {
        this.departamentRepository = departamentRepository;
    }

    public Departament createDepartament(Departament departament){
        Departament savedDepartament = departamentRepository.save(departament);
        return savedDepartament;
    }

    public List<Departament> getActiveDepartaments(){
        List<Departament> departaments = departamentRepository.findAll();
        return departaments;
    }
}
