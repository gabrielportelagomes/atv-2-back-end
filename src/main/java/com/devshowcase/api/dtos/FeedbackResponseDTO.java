package com.devshowcase.api.dtos;

public record FeedbackResponseDTO(Long id, String comment, Integer rating, Long projectId) {}