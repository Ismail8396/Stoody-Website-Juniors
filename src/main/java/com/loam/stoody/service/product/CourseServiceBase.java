package com.loam.stoody.service.product;

import com.loam.stoody.dto.api.request.course.CourseOverviewRequestDTO;
import com.loam.stoody.global.constants.MiscConstants;
import com.loam.stoody.model.product.course.approved.ApprovedCourse;
import com.loam.stoody.model.product.course.core.CourseCore;
import com.loam.stoody.model.product.course.pending.PendingCourse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CourseServiceBase extends CourseCoreService {
    private final PendingCourseService pendingCourseService;
    private final ApprovedCourseService approvedCourseService;

    // -> Course Overview

    public <T extends CourseCore> CourseOverviewRequestDTO mapCourseCoreChildToOverviewDTO(T e){
        try {
            CourseOverviewRequestDTO overview = new CourseOverviewRequestDTO();
            overview.setId(e.getId());
            overview.setName(e.getTitle());
            overview.setStatus(e.getCourseStatus().toString());
            overview.setCreatedAt(e.getCreatedDate().format(MiscConstants.standardDateTimeFormat));
            overview.setCourseThumbnail(e.getThumbnail() != null ? e.getThumbnail().getUrl() : null);
            overview.setAuthorUsername(e.getAuthor() != null ? e.getAuthor().getUsername() : null);
            return overview;
        } catch (Exception ignored) {
        }
        return null;
    }

    public List<CourseOverviewRequestDTO> getAllCourseOverview() {
        List<CourseOverviewRequestDTO> list = new ArrayList<>();

        pendingCourseService.getAllPendingCourses(PendingCourse.class).forEach(e -> {
            CourseOverviewRequestDTO overview = mapCourseCoreChildToOverviewDTO(e);
            if(overview != null) {
                final ApprovedCourse approvedCourse = pendingCourseService.findApprovedCourseOfPending(e);
                overview.setApprovedCourseId(approvedCourse != null
                        ? approvedCourse.getId() : null);
                list.add(overview);
            }
        });
        approvedCourseService.getAllApprovedCourses(ApprovedCourse.class).forEach(e -> {
            CourseOverviewRequestDTO overview = mapCourseCoreChildToOverviewDTO(e);
            if(overview != null) {
                final PendingCourse pendingCourse = e.getPendingCourse();
                overview.setPendingCourseId(pendingCourse != null ? pendingCourse.getId() : null);
                list.add(overview);
            }
        });
        return list;
    }

    public List<CourseOverviewRequestDTO> getLimitedCourseOverview(Long limit) {
        return getAllCourseOverview().stream().limit(limit).toList();
    }
}
