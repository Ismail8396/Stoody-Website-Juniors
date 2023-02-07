package com.loam.stoody.dto.api.request.course.pending;

import com.loam.stoody.dto.api.request.course.core.CourseCoreDTO;
import lombok.Data;

import java.util.List;

@Data
public class PendingCourseDTO extends CourseCoreDTO {
    private Long approvedCourseId;
    private List<PendingCourseSectionDTO> sections;
}