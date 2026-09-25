package com.devshowcase.api.controller;

import com.devshowcase.api.dtos.TechnologyRequestDTO;
import com.devshowcase.api.dtos.TechnologyResponseDTO;
import com.devshowcase.api.service.TechnologyService;
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
@RequestMapping("/api/technologies")
@Tag(name = "Tecnologias", description = "Endpoints para gerenciamento das tecnologias utilizadas nos projetos")
public class TechnologyController {

    @Autowired
    private TechnologyService technologyService;

    @Operation(summary = "Criar uma nova tecnologia", description = "Cadastra uma nova tecnologia que poderá ser vinculada aos projetos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tecnologia criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação nos dados enviados (ex: nome vazio)")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TechnologyResponseDTO createTechnology(@Valid @RequestBody TechnologyRequestDTO dto) {
        return technologyService.createTechnology(dto);
    }

    @Operation(summary = "Listar tecnologias", description = "Retorna uma lista com todas as tecnologias cadastradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de tecnologias retornada com sucesso")
    })
    @GetMapping
    public List<TechnologyResponseDTO> listTechnologies() {
        return technologyService.listTechnologies();
    }
}