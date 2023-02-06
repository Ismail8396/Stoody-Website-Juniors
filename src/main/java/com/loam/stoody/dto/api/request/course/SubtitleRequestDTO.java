package com.loam.stoody.dto.api.request.course;

import lombok.Data;

@Data
public class SubtitleRequestDTO {
    private String languageCode;
    private Long userFileId;
}
