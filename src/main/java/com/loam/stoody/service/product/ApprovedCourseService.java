package com.loam.stoody.service.product;

import com.loam.stoody.model.product.course.approved.ApprovedCourse;
import com.loam.stoody.model.product.course.approved.ApprovedCourseLecture;
import com.loam.stoody.model.product.course.approved.ApprovedCourseSection;
import com.loam.stoody.model.product.course.pending.PendingCourse;
import com.loam.stoody.repository.product.approved.ApprovedCourseLectureRepository;
import com.loam.stoody.repository.product.approved.ApprovedCourseRepository;
import com.loam.stoody.repository.product.approved.ApprovedCourseSectionRepository;
import com.loam.stoody.repository.product.pending.PendingCourseLectureRepository;
import com.loam.stoody.repository.product.pending.PendingCourseRepository;
import com.loam.stoody.repository.product.pending.PendingCourseSectionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ApprovedCourseService extends CourseCoreService{
    //private final CommentRepository commentRepository;
    //private final CourseRatingRepository courseRatingRepository;
    //private final PurchasedCourseRepository purchasedCourseRepository;

    private final PendingCourseRepository pendingCourseRepository;
    private final PendingCourseLectureRepository pendingCourseLectureRepository;
    private final PendingCourseSectionRepository pendingCourseSectionRepository;

    private final ApprovedCourseRepository approvedCourseRepository;
    private final ApprovedCourseLectureRepository approvedCourseLectureRepository;
    private final ApprovedCourseSectionRepository approvedCourseSectionRepository;

    public List<ApprovedCourse> getAllApprovedCourses() {
        return approvedCourseRepository.findAll();
    }

    // Pending course should be in database inherited by an Author!
    public ApprovedCourse submitPendingCourse(PendingCourse pendingCourse) {
        try {
            ApprovedCourse approvedCourse;

            if(pendingCourse.getApprovedCourse() == null){
                approvedCourse = convertShallow(pendingCourse, ApprovedCourse.class);
            }else{
                approvedCourse = approvedCourseRepository.findById(pendingCourse.getApprovedCourse().getId()).orElse(null);
                if(approvedCourse == null)
                    approvedCourse = convertShallow(pendingCourse, ApprovedCourse.class);
            }

            approvedCourse.setPendingCourse(pendingCourse);
            // Save and update the instance
            approvedCourse = approvedCourseRepository.save(approvedCourse);

            // Save the pending course, so it may not lose the track of approved course
            pendingCourse.setApprovedCourse(approvedCourse);
            pendingCourseRepository.save(pendingCourse);

            // Copy Sections & Lectures
            for (var i : pendingCourseSectionRepository.findAll()) {
                if (i.getPendingCourse().getId().equals(pendingCourse.getId())) {
                    ApprovedCourseSection approvedCourseSection = convertShallow(i, ApprovedCourseSection.class);
                    approvedCourseSection = approvedCourseSectionRepository.save(approvedCourseSection);
                    for (var j : pendingCourseLectureRepository.findAll()) {
                        if (j.getPendingCourseSection().getId().equals(i.getPendingCourse().getId())) {
                            ApprovedCourseLecture approvedCourseLecture = convertShallow(j, ApprovedCourseLecture.class);
                            approvedCourseLecture = approvedCourseLectureRepository.save(approvedCourseLecture);
                        }
                    }
                }
            }

            return approvedCourse;
        } catch (ReflectiveOperationException e) {
            System.out.println("Course could not be approved! Reason: " + e.getMessage());
        }
        return null;
    }
}
