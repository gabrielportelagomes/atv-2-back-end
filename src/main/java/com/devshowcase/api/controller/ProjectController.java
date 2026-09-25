package com.devshowcase.api.controller;

import com.devshowcase.api.dtos.ProjectRequestDTO;
import com.devshowcase.api.dtos.ProjectResponseDTO;
import com.devshowcase.api.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@Tag(name = "Projetos", description = "Endpoints para gerenciamento de projetos do portfólio")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Operation(summary = "Criar um novo projeto", description = "Cadastra um novo projeto e o vincula a um perfil e suas tecnologias")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Projeto criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação nos dados enviados"),
            @ApiResponse(responseCode = "404", description = "Perfil informado não encontrado")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectResponseDTO createProject(@Valid @RequestBody ProjectRequestDTO dto) {
        return projectService.createProject(dto);
    }

    @Operation(summary = "Listar projetos", description = "Retorna uma lista com todos os projetos cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de projetos retornada com sucesso")
    })
    @GetMapping
    public List<ProjectResponseDTO> listProjects() {
        return projectService.listProjects();
    }
}