package com.devshowcase.api.service;

import com.devshowcase.api.dtos.*;
import com.devshowcase.api.model.*;
import com.devshowcase.api.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProjectService {

    @Autowired private ProjectRepository projectRepository;
    @Autowired private ProfileRepository profileRepository;
    @Autowired private TechnologyRepository technologyRepository;
    @Autowired private FeedbackRepository feedbackRepository;

    @Transactional
    public ProjectResponseDTO createProject(ProjectRequestDTO dto) {
        Profile profile = profileRepository.findById(dto.profileId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Perfil não encontrado"));

        Project project = new Project();
        project.setTitle(dto.title());
        project.setDescription(dto.description());
        project.setRepositoryUrl(dto.repositoryUrl());
        project.setProfile(profile);
        project.setUpvotes(0);
        project.setAverageRating(0.0);

        if (dto.technologyIds() != null && !dto.technologyIds().isEmpty()) {
            List<Technology> techs = technologyRepository.findAllById(dto.technologyIds());
            project.setTechnologies(techs);
        }

        Project saved = projectRepository.save(project);
        List<Long> techIds = saved.getTechnologies() != null ? saved.getTechnologies().stream().map(Technology::getId).toList() : List.of();

        return new ProjectResponseDTO(saved.getId(), saved.getTitle(), saved.getDescription(), saved.getRepositoryUrl(), profile.getId(), techIds, saved.getUpvotes(), saved.getAverageRating());
    }

    @Transactional(readOnly = true)
    public List<ProjectResponseDTO> listAllProjects() {
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
                            techIds,
                            p.getUpvotes(),
                            p.getAverageRating()
                    );
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public Page<ProjectResponseDTO> listProjects(String technology, Pageable pageable) {
        Page<Project> page;
        if (technology != null && !technology.isBlank()) {
            page = projectRepository.findByTechnologiesNameContainingIgnoreCase(technology, pageable);
        } else {
            page = projectRepository.findAll(pageable);
        }

        return page.map(p -> {
            List<Long> techIds = p.getTechnologies() != null ? p.getTechnologies().stream().map(Technology::getId).toList() : List.of();
            return new ProjectResponseDTO(p.getId(), p.getTitle(), p.getDescription(), p.getRepositoryUrl(), p.getProfile().getId(), techIds, p.getUpvotes(), p.getAverageRating());
        });
    }

    @Transactional
    public void incrementUpvote(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Projeto não encontrado com ID: " + projectId));
        project.setUpvotes(project.getUpvotes() + 1);
        projectRepository.save(project);
    }

    @Transactional
    public FeedbackResponseDTO addFeedback(Long projectId, FeedbackRequestDTO dto) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Projeto não encontrado com ID: " + projectId));

        Feedback feedback = new Feedback();
        feedback.setComment(dto.comment());
        feedback.setRating(dto.rating());
        feedback.setProject(project);
        Feedback saved = feedbackRepository.save(feedback);

        Double average = feedbackRepository.calculateAverageRatingByProjectId(projectId);
        project.setAverageRating(Math.round(average * 10.0) / 10.0);
        projectRepository.save(project);

        return new FeedbackResponseDTO(saved.getId(), saved.getComment(), saved.getRating(), projectId);
    }
}