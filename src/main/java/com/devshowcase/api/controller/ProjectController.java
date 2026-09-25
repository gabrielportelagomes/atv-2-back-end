package com.devshowcase.api.controller;

import com.devshowcase.api.dtos.*;
import com.devshowcase.api.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@Tag(name = "Projetos", description = "Endpoints para gerenciamento, feedbacks e upvotes de projetos")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectResponseDTO createProject(@Valid @RequestBody ProjectRequestDTO dto) {
        return projectService.createProject(dto);
    }

    @Operation(summary = "Listar todos os projetos (Lista simples)", description = "Retorna todos os projetos cadastrados numa lista simples, sem paginação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de projetos devolvida com sucesso")
    })
    @GetMapping("/all")
    public List<ProjectResponseDTO> listAllProjects() {
        return projectService.listAllProjects();
    }

    @Operation(summary = "Listar projetos", description = "Retorna projetos paginados com opção de filtro por nome da tecnologia")
    @GetMapping
    public Page<ProjectResponseDTO> listProjects(
            @RequestParam(required = false) String technology,
            @ParameterObject Pageable pageable) {
        return projectService.listProjects(technology, pageable);
    }

    @Operation(summary = "Adicionar feedback", description = "Adiciona um comentário e nota ao projeto, recalculando a média geral")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Feedback adicionado"),
            @ApiResponse(responseCode = "404", description = "Projeto não encontrado")
    })
    @PostMapping("/{id}/feedbacks")
    @ResponseStatus(HttpStatus.CREATED)
    public FeedbackResponseDTO addFeedback(@PathVariable Long id, @Valid @RequestBody FeedbackRequestDTO dto) {
        return projectService.addFeedback(id, dto);
    }

    @Operation(summary = "Dar Upvote", description = "Incrementa o número de curtidas de um projeto")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Upvote computado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Projeto não encontrado")
    })
    @PutMapping("/{id}/upvote")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void upvoteProject(@PathVariable Long id) {
        projectService.incrementUpvote(id);
    }
}