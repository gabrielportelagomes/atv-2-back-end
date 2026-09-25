package com.devshowcase.api.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ProfileRequestDTO(
        @NotBlank(message = "O nome não pode estar vazio") String name,
        @Email(message = "Email inválido") @NotBlank String email,
        String bio
) {}