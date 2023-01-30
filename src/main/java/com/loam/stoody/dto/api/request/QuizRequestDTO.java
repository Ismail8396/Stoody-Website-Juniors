package com.loam.stoody.dto.api.request;

import lombok.Data;

import java.util.List;

@Data
public class QuizRequestDTO {

    private Long id;
    private List<QuizQuestionRequestDTO> questions;
}
