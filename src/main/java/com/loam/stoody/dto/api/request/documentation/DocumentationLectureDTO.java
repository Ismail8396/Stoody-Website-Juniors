package com.loam.stoody.dto.api.request.documentation;

import lombok.Data;

@Data
public class DocumentationLectureDTO {
    private Long id;
    private String name;

    private String description;
    private String content;
    private Long sectionId;

}
