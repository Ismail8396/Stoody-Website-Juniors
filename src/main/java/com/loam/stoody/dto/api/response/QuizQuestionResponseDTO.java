package com.loam.stoody.dto.api.response;

import lombok.Data;

import java.util.List;

@Data
public class QuizQuestionResponseDTO {
    private Long id;
    private String question;
    private Long quizId;
    private List<QuizQuestionAnswerResponseDTO> answers;
}
