package com.loam.stoody.dto.api.request.course.approved;

import com.loam.stoody.dto.api.request.course.core.CourseCoreSectionDTO;
import com.loam.stoody.dto.api.request.course.pending.PendingCourseLectureDTO;
import lombok.Data;

import java.util.List;

@Data
public class ApprovedCourseSectionDTO extends CourseCoreSectionDTO {

    private List<ApprovedCourseLectureDTO> lectures;
}
