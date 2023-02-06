package com.loam.stoody.dto.api.request.course.core;

import com.loam.stoody.dto.api.request.quiz.QuizRequestDTO;
import com.loam.stoody.dto.api.request.course.VideoRequestDTO;
import com.loam.stoody.dto.api.request.file.UserFileRequestDTO;
import lombok.Data;

@Data
public class CourseCoreLectureDTO {
    private String title;

    private UserFileRequestDTO userFile;
    private VideoRequestDTO video;
    private String description;
    private QuizRequestDTO quiz;

    private Boolean locked;
    private Boolean uiCollapsed;
}
