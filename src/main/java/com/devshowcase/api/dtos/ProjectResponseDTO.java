package com.devshowcase.api.dtos;

import java.util.List;

public record ProjectResponseDTO(Long id, String title, String description, String repositoryUrl, Long profileId, List<Long> technologyIds) {}
