package com.loam.stoody.dto.api.request.documentation;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class DocumentationLectureDTO {
    private Long id;
    private String name;

    private String metaDescription;
    private String metaKeywords;
    private String metaAuthor;

    private String urlKey;
    private String content;
    private Long sectionId;
}
