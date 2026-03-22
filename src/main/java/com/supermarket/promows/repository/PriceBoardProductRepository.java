package com.supermarket.promows.repository;

import com.supermarket.promows.model.PriceBoardProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PriceBoardProductRepository extends JpaRepository<PriceBoardProduct, Long> {

    List<PriceBoardProduct> findAllByOrderBySortOrderAscIdAsc();

    List<PriceBoardProduct> findAllByActiveTrueOrderBySortOrderAscIdAsc();
}
