package com.loam.stoody.dto.api.request.course.pending;

import com.loam.stoody.dto.api.request.course.core.CourseCoreDTO;
import lombok.Data;

@Data
public class PendingCourseDTO extends CourseCoreDTO {
    private Long approvedCourseId;
}