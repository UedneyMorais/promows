package com.supermarket.promows.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.supermarket.promows.model.CompanyBackgroundImage;

public interface CompanyBackgroundImageRepository extends JpaRepository<CompanyBackgroundImage, Long> {

    List<CompanyBackgroundImage> findByCompanyIdOrderBySortOrderAscIdAsc(Long companyId);

    long countByCompanyId(Long companyId);
}
