package com.loam.stoody.dto.api.request.course.approved;

import com.loam.stoody.dto.api.request.course.core.CourseCoreDTO;
import lombok.Data;

@Data
public class ApprovedCourseDTO extends CourseCoreDTO {
    private Long pendingCourseId;
}
