package com.supermarket.promows.controller;

import com.supermarket.promows.dto.PriceBoardProductDTO;
import com.supermarket.promows.service.PriceBoardProductService;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/price-products")
public class PriceBoardProductController {

    private final PriceBoardProductService priceBoardProductService;

    public PriceBoardProductController(PriceBoardProductService priceBoardProductService) {
        this.priceBoardProductService = priceBoardProductService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<PriceBoardProductDTO> create(
            @RequestParam("product") String productJson,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        return new ResponseEntity<>(priceBoardProductService.create(productJson, file), HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<PriceBoardProductDTO> update(
            @PathVariable Long id,
            @RequestParam("product") String productJson,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        return ResponseEntity.ok(priceBoardProductService.update(id, productJson, file));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        priceBoardProductService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public List<PriceBoardProductDTO> findAll() {
        return priceBoardProductService.findAll();
    }

    /** Itens ativos para o painel (TV) — ordenados */
    @GetMapping(value = "/board", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public List<PriceBoardProductDTO> board() {
        return priceBoardProductService.findAllActiveForBoard();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public PriceBoardProductDTO findById(@PathVariable Long id) {
        return priceBoardProductService.findById(id);
    }
}
