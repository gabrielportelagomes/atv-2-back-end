package com.devshowcase.api.service;

import com.devshowcase.api.dtos.TechnologyRequestDTO;
import com.devshowcase.api.dtos.TechnologyResponseDTO;
import com.devshowcase.api.model.Technology;
import com.devshowcase.api.repository.TechnologyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TechnologyService {

    @Autowired
    private TechnologyRepository technologyRepository;

    @Transactional
    public TechnologyResponseDTO createTechnology(TechnologyRequestDTO dto) {
        Technology tech = new Technology();
        tech.setName(dto.name());

        Technology saved = technologyRepository.save(tech);
        return new TechnologyResponseDTO(saved.getId(), saved.getName());
    }

    @Transactional(readOnly = true)
    public List<TechnologyResponseDTO> listTechnologies() {
        return technologyRepository.findAll().stream()
                .map(t -> new TechnologyResponseDTO(t.getId(), t.getName()))
                .toList();
    }
}