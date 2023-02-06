package com.loam.stoody.dto.api.request.quiz;

import lombok.Data;

import java.util.List;

@Data
public class QuizQuestionRequestDTO {
    private Long id;
    private String question;
    private Long timeoutInSeconds;
    private Long quizId;
    private List<QuizQuestionAnswerRequestDTO> answers;
}
