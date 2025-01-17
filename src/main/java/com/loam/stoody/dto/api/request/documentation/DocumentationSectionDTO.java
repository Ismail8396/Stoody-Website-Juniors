package com.loam.stoody.dto.api.request.documentation;

import lombok.Data;

@Data
public class DocumentationSectionDTO {
    private Long id;
    private String name;
    private String description;
    private String text;
    private String thumbnailUrl;

    private DocumentationCategoryDTO documentationCategory;

    private Long visitCount;
}
