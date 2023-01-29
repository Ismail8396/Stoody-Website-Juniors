package com.loam.stoody.dto.api.request;

import lombok.Data;

@Data
public class QuizQuestionAnswerRequestDTO {
    private Long id;
    private Long quizQuestionId;
    private String answer;
    private Boolean isTrue;
}
