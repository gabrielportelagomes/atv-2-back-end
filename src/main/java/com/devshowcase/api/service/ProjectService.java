package com.devshowcase.api.service;

import com.devshowcase.api.dtos.ProjectRequestDTO;
import com.devshowcase.api.dtos.ProjectResponseDTO;
import com.devshowcase.api.model.Profile;
import com.devshowcase.api.model.Project;
import com.devshowcase.api.model.Technology;
import com.devshowcase.api.repository.ProfileRepository;
import com.devshowcase.api.repository.ProjectRepository;
import com.devshowcase.api.repository.TechnologyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private TechnologyRepository technologyRepository;

    @Transactional
    public ProjectResponseDTO createProject(ProjectRequestDTO dto) {
        Profile profile = profileRepository.findById(dto.profileId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Perfil não encontrado"));

        Project project = new Project();
        project.setTitle(dto.title());
        project.setDescription(dto.description());
        project.setRepositoryUrl(dto.repositoryUrl());
        project.setProfile(profile);

        if (dto.technologyIds() != null && !dto.technologyIds().isEmpty()) {
            List<Technology> techs = technologyRepository.findAllById(dto.technologyIds());
            project.setTechnologies(techs);
        }

        Project saved = projectRepository.save(project);

        List<Long> techIds = saved.getTechnologies() != null
                ? saved.getTechnologies().stream().map(Technology::getId).toList()
                : List.of();

        return new ProjectResponseDTO(
                saved.getId(),
                saved.getTitle(),
                saved.getDescription(),
                saved.getRepositoryUrl(),
                profile.getId(),
                techIds
        );
    }

    @Transactional(readOnly = true)
    public List<ProjectResponseDTO> listProjects() {
        return projectRepository.findAll().stream()
                .map(p -> {

                    List<Long> techIds = p.getTechnologies() != null
                            ? p.getTechnologies().stream().map(Technology::getId).toList()
                            : List.of();

                    return new ProjectResponseDTO(
                            p.getId(),
                            p.getTitle(),
                            p.getDescription(),
                            p.getRepositoryUrl(),
                            p.getProfile().getId(),
                            techIds
                    );
                })
                .toList();
    }
}