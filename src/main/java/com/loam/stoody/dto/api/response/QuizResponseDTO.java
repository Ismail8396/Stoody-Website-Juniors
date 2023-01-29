package com.loam.stoody.dto.api.response;

import lombok.Data;

import java.util.List;

@Data
public class QuizResponseDTO {

    private Long id;
    private List<QuizQuestionResponseDTO> quizQuestions;
}
