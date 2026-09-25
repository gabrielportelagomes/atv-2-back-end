package com.devshowcase.api.repository;

import com.devshowcase.api.model.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Page<Project> findByTechnologiesNameContainingIgnoreCase(String technologyName, Pageable pageable);
}