package com.devshowcase.api.controller;

import com.devshowcase.api.dtos.ProfileRequestDTO;
import com.devshowcase.api.dtos.ProfileResponseDTO;
import com.devshowcase.api.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profiles")
@Tag(name = "Perfis", description = "Endpoints para gerenciamento de perfis de desenvolvedores")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @Operation(summary = "Criar um novo perfil", description = "Cadastra um novo perfil de desenvolvedor na plataforma")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Perfil criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação nos dados enviados")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProfileResponseDTO createProfile(@Valid @RequestBody ProfileRequestDTO dto) {
        return profileService.createProfile(dto);
    }

    @Operation(summary = "Buscar perfil por ID", description = "Retorna os detalhes de um perfil específico baseado no seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil encontrado"),
            @ApiResponse(responseCode = "404", description = "Perfil não encontrado")
    })
    @GetMapping("/{id}")
    public ProfileResponseDTO getProfile(@PathVariable Long id) {
        return profileService.getProfile(id);
    }
}