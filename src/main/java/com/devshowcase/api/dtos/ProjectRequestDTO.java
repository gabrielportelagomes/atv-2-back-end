package com.devshowcase.api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;

import java.util.List;

public record ProjectRequestDTO(
        @NotBlank(message = "O título é obrigatório") String title,
        @NotBlank(message = "A descrição é obrigatória") String description,
        @URL(message = "URL do repositório inválida") String repositoryUrl,
        @NotNull(message = "O ID do perfil é obrigatório") Long profileId,
        List<Long> technologyIds
) {}
