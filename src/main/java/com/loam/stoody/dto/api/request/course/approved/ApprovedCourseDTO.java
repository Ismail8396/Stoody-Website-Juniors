package com.loam.stoody.dto.api.request.course.approved;

import com.loam.stoody.dto.api.request.course.core.CourseCoreDTO;
import com.loam.stoody.dto.api.request.course.pending.PendingCourseSectionDTO;
import lombok.Data;

import java.util.List;

@Data
public class ApprovedCourseDTO extends CourseCoreDTO {
    private Long pendingCourseId;
    private List<ApprovedCourseSectionDTO> sections;

}
