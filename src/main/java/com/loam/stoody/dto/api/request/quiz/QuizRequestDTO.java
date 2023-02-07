package com.loam.stoody.dto.api.request.quiz;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
public class QuizRequestDTO {
    private Long id;
    private String name;
    private String thumbnailUrl;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<QuizQuestionRequestDTO> questions;
}
