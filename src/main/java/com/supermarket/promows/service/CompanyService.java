package com.supermarket.promows.service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.supermarket.promows.dto.CompanyBackgroundImageDto;
import com.supermarket.promows.dto.CompanyCreateRequest;
import com.supermarket.promows.dto.CompanyDto;
import com.supermarket.promows.dto.CompanyPriceBoardBackgroundRequest;
import com.supermarket.promows.dto.CompanyPublicDto;
import com.supermarket.promows.dto.CompanyUpdateRequest;
import com.supermarket.promows.exception.CompanyNotFoundException;
import com.supermarket.promows.model.Company;
import com.supermarket.promows.model.CompanyBackgroundImage;
import com.supermarket.promows.repository.CompanyBackgroundImageRepository;
import com.supermarket.promows.repository.CompanyRepository;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyBackgroundImageRepository backgroundImageRepository;
    private final FileSystemStorageService fileSystemStorageService;

    public CompanyService(
            CompanyRepository companyRepository,
            CompanyBackgroundImageRepository backgroundImageRepository,
            FileSystemStorageService fileSystemStorageService) {
        this.companyRepository = companyRepository;
        this.backgroundImageRepository = backgroundImageRepository;
        this.fileSystemStorageService = fileSystemStorageService;
    }

    @Transactional(readOnly = true)
    public Optional<CompanyDto> getCompany() {
        return companyRepository.findFirstByOrderByIdAsc().map(this::toDto);
    }

    @Transactional(readOnly = true)
    public CompanyPublicDto getPublicProfile() {
        return companyRepository.findFirstByOrderByIdAsc()
                .map(this::toPublicDto)
                .orElse(CompanyPublicDto.empty());
    }

    @Transactional
    public CompanyDto create(CompanyCreateRequest req) {
        if (companyRepository.count() > 0) {
            throw new IllegalArgumentException(
                    "Já existe uma empresa cadastrada. Atualize com PUT /api/company/{id}.");
        }
        Company c = new Company();
        c.setName(req.getName().trim());
        c.setAddress(req.getAddress() != null && !req.getAddress().isBlank() ? req.getAddress().trim() : null);
        c.setPhone(req.getPhone() != null && !req.getPhone().isBlank() ? req.getPhone().trim() : null);
        Company saved = companyRepository.save(c);
        return toDto(saved);
    }

    @Transactional
    public CompanyDto update(Long id, CompanyUpdateRequest req) {
        Company c = companyRepository.findById(id).orElseThrow(() -> new CompanyNotFoundException(id));
        if (req.getName() != null && !req.getName().isBlank()) {
            c.setName(req.getName().trim());
        }
        if (req.getAddress() != null) {
            c.setAddress(req.getAddress().isBlank() ? null : req.getAddress().trim());
        }
        if (req.getPhone() != null) {
            c.setPhone(req.getPhone().isBlank() ? null : req.getPhone().trim());
        }
        return toDto(companyRepository.save(c));
    }

    @Transactional
    public CompanyDto uploadLogo(Long id, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Arquivo de logo é obrigatório.");
        }
        Company c = companyRepository.findById(id).orElseThrow(() -> new CompanyNotFoundException(id));
        if (c.getLogoFilename() != null) {
            fileSystemStorageService.deleteIfExists(c.getLogoFilename());
        }
        String fn = fileSystemStorageService.store(file);
        c.setLogoFilename(fn);
        return toDto(companyRepository.save(c));
    }

    @Transactional
    public CompanyDto addBackground(Long companyId, MultipartFile file, String label) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Arquivo de imagem é obrigatório.");
        }
        Company c = companyRepository.findById(companyId).orElseThrow(() -> new CompanyNotFoundException(companyId));
        String fn = fileSystemStorageService.store(file);
        int nextOrder = backgroundImageRepository.findByCompanyIdOrderBySortOrderAscIdAsc(companyId).stream()
                .mapToInt(CompanyBackgroundImage::getSortOrder)
                .max()
                .orElse(-1) + 1;
        CompanyBackgroundImage img = new CompanyBackgroundImage();
        img.setCompany(c);
        img.setFilename(fn);
        img.setLabel(label != null && !label.isBlank() ? label.trim() : null);
        img.setSortOrder(nextOrder);
        backgroundImageRepository.save(img);
        if (c.getPriceBoardBackground() == null) {
            c.setPriceBoardBackground(img);
        }
        c.setUseDefaultPriceBoardBackground(false);
        companyRepository.save(c);
        return toDto(companyRepository.findById(companyId).orElseThrow(() -> new CompanyNotFoundException(companyId)));
    }

    @Transactional
    public CompanyDto deleteBackground(Long companyId, Long backgroundId) {
        Company c = companyRepository.findById(companyId).orElseThrow(() -> new CompanyNotFoundException(companyId));
        CompanyBackgroundImage img = backgroundImageRepository.findById(backgroundId)
                .orElseThrow(() -> new IllegalArgumentException("Imagem de fundo não encontrada."));
        if (!img.getCompany().getId().equals(companyId)) {
            throw new IllegalArgumentException("Imagem não pertence a esta empresa.");
        }
        if (c.getPriceBoardBackground() != null && c.getPriceBoardBackground().getId().equals(backgroundId)) {
            c.setPriceBoardBackground(null);
        }
        fileSystemStorageService.deleteIfExists(img.getFilename());
        backgroundImageRepository.delete(img);
        companyRepository.flush();
        return toDto(companyRepository.findById(companyId).orElseThrow(() -> new CompanyNotFoundException(companyId)));
    }

    @Transactional
    public CompanyDto setPriceBoardBackground(Long companyId, CompanyPriceBoardBackgroundRequest req) {
        Company c = companyRepository.findById(companyId).orElseThrow(() -> new CompanyNotFoundException(companyId));
        if (req.getBackgroundImageId() == null) {
            c.setPriceBoardBackground(null);
            c.setUseDefaultPriceBoardBackground(true);
        } else {
            CompanyBackgroundImage bg = backgroundImageRepository.findById(req.getBackgroundImageId())
                    .orElseThrow(() -> new IllegalArgumentException("Imagem não encontrada."));
            if (!bg.getCompany().getId().equals(companyId)) {
                throw new IllegalArgumentException("Imagem não pertence a esta empresa.");
            }
            c.setPriceBoardBackground(bg);
            c.setUseDefaultPriceBoardBackground(false);
        }
        return toDto(companyRepository.save(c));
    }

    private CompanyPublicDto toPublicDto(Company c) {
        String logoUrl = c.getLogoFilename() != null ? fileSystemStorageService.getUrl(c.getLogoFilename()) : null;
        String bgUrl = resolvePriceBoardBackgroundUrl(c);
        return new CompanyPublicDto(c.getName(), c.getAddress(), c.getPhone(), logoUrl, bgUrl);
    }

    /**
     * URL do fundo do painel: seleção explícita; se não houver, usa a primeira imagem cadastrada
     * (evita fundo vazio quando o usuário enviou arquivo mas não clicou em "Usar no painel TV").
     */
    private String resolvePriceBoardBackgroundUrl(Company c) {
        if (c.isUseDefaultPriceBoardBackground()) {
            return null;
        }
        if (c.getPriceBoardBackground() != null && c.getPriceBoardBackground().getFilename() != null) {
            return fileSystemStorageService.getUrl(c.getPriceBoardBackground().getFilename());
        }
        List<CompanyBackgroundImage> list = backgroundImageRepository.findByCompanyIdOrderBySortOrderAscIdAsc(c.getId());
        if (!list.isEmpty()) {
            return fileSystemStorageService.getUrl(list.get(0).getFilename());
        }
        return null;
    }

    private CompanyDto toDto(Company c) {
        CompanyDto dto = new CompanyDto();
        dto.setId(c.getId());
        dto.setName(c.getName());
        dto.setAddress(c.getAddress());
        dto.setPhone(c.getPhone());
        dto.setLogoUrl(c.getLogoFilename() != null ? fileSystemStorageService.getUrl(c.getLogoFilename()) : null);
        dto.setSelectedPriceBoardBackgroundId(
                c.getPriceBoardBackground() != null ? c.getPriceBoardBackground().getId() : null);
        for (CompanyBackgroundImage img : c.getBackgroundImages()) {
            dto.getBackgroundImages().add(new CompanyBackgroundImageDto(
                    img.getId(),
                    img.getLabel(),
                    img.getSortOrder(),
                    fileSystemStorageService.getUrl(img.getFilename())));
        }
        dto.getBackgroundImages().sort(Comparator
                .comparingInt(CompanyBackgroundImageDto::getSortOrder)
                .thenComparing(CompanyBackgroundImageDto::getId));
        return dto;
    }
}
