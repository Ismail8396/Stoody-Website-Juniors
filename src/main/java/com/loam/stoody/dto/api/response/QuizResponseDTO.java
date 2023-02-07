package com.loam.stoody.dto.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
public class QuizResponseDTO {

    private Long id;
    private String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<QuizQuestionResponseDTO> questions;
}
