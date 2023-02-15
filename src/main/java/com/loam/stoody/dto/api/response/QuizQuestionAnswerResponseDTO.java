package com.loam.stoody.dto.api.response;

import lombok.Data;

@Data
@Deprecated
public class QuizQuestionAnswerResponseDTO {
    private Long id;
    private Long quizQuestionId;
    private String answer;
    private Boolean isTrue;
}
