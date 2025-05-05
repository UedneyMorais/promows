package com.supermarket.promows.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.supermarket.promows.dto.DepartmentDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String departmentName;

    @OneToMany(mappedBy = "department")
    @JsonIgnore // Isso evita a serialização da lista de promoções
    private List<Promotion> promotions = new ArrayList<Promotion>();

    public Department() {
    }


    public Department(DepartmentDTO departmentDTO) {
        this.departmentName = departmentDTO.getDepartmentName();
    }
    
}
