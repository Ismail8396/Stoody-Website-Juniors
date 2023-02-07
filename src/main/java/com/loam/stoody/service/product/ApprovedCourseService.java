package com.loam.stoody.service.product;

import com.loam.stoody.dto.api.request.course.VideoRequestDTO;
import com.loam.stoody.dto.api.request.course.approved.ApprovedCourseDTO;
import com.loam.stoody.dto.api.request.course.approved.ApprovedCourseLectureDTO;
import com.loam.stoody.dto.api.request.course.approved.ApprovedCourseSectionDTO;
import com.loam.stoody.dto.api.request.course.pending.PendingCourseDTO;
import com.loam.stoody.dto.api.request.course.pending.PendingCourseLectureDTO;
import com.loam.stoody.dto.api.request.course.pending.PendingCourseSectionDTO;
import com.loam.stoody.dto.api.request.file.UserFileRequestDTO;
import com.loam.stoody.model.product.course.approved.ApprovedCourse;
import com.loam.stoody.model.product.course.approved.ApprovedCourseLecture;
import com.loam.stoody.model.product.course.approved.ApprovedCourseSection;
import com.loam.stoody.model.product.course.pending.PendingCourse;
import com.loam.stoody.model.product.course.pending.PendingCourseLecture;
import com.loam.stoody.model.product.course.pending.PendingCourseSection;
import com.loam.stoody.repository.product.approved.ApprovedCourseLectureRepository;
import com.loam.stoody.repository.product.approved.ApprovedCourseRepository;
import com.loam.stoody.repository.product.approved.ApprovedCourseSectionRepository;
import com.loam.stoody.repository.product.pending.PendingCourseLectureRepository;
import com.loam.stoody.repository.product.pending.PendingCourseRepository;
import com.loam.stoody.repository.product.pending.PendingCourseSectionRepository;
import com.loam.stoody.service.communication.file.UserFileService;
import com.loam.stoody.service.communication.video.VideoService;
import com.loam.stoody.service.product.quiz.QuizService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ApprovedCourseService extends CourseCoreService {
    //private final CommentRepository commentRepository;
    //private final CourseRatingRepository courseRatingRepository;
    //private final PurchasedCourseRepository purchasedCourseRepository;

    private final PendingCourseRepository pendingCourseRepository;
    private final PendingCourseLectureRepository pendingCourseLectureRepository;
    private final PendingCourseSectionRepository pendingCourseSectionRepository;

    private final ApprovedCourseRepository approvedCourseRepository;
    private final ApprovedCourseLectureRepository approvedCourseLectureRepository;
    private final ApprovedCourseSectionRepository approvedCourseSectionRepository;
    private final VideoService videoService;
    private final UserFileService userFileService;
    private final QuizService quizService;

    public List<ApprovedCourseDTO> getAllApprovedCourses() {
        return approvedCourseRepository.findAll().stream().map(this::mapCourseEntityToRequest).collect(Collectors.toList());
    }

    // Pending course should be in database inherited by an Author!
    @Transactional
    public ApprovedCourseDTO submitPendingCourse(PendingCourse pendingCourse) {
        try {
            ApprovedCourse approvedCourse = convertShallow(pendingCourse, ApprovedCourse.class);
            approvedCourse.setId(null);
            approvedCourse.setPendingCourse(pendingCourse);
            approvedCourse.setCourseCategory(pendingCourse.getCourseCategory());
            approvedCourse = approvedCourseRepository.saveAndFlush(approvedCourse);

            List<ApprovedCourseSection> approvedCourseSections = new ArrayList<>();
            List<ApprovedCourseLecture> approvedCourseLectures = new ArrayList<>();

            List<PendingCourseSection> pendingCourseSections =
                    pendingCourseSectionRepository.findAllByPendingCourse_Id(pendingCourse.getId());
            if (!CollectionUtils.isEmpty(pendingCourseSections)) {
                ApprovedCourse finalApprovedCourse = approvedCourse;
                pendingCourseSections.forEach(pendSec -> {

                    ApprovedCourseSection approvedCourseSection = new ApprovedCourseSection();
                    BeanUtils.copyProperties(pendSec, approvedCourseSection);
                    approvedCourseSection.setApprovedCourse(finalApprovedCourse);
                    approvedCourseSection.setId(null);
                    approvedCourseSectionRepository.saveAndFlush(approvedCourseSection);
                    approvedCourseSections.add(approvedCourseSection);

                    List<PendingCourseLecture> pendingCourseLectures =
                            pendingCourseLectureRepository.findAllByPendingCourseSection_Id(pendSec.getId());
                    if (!CollectionUtils.isEmpty(pendingCourseLectures)) {
                        pendingCourseLectures.forEach(pendLec -> {

                            ApprovedCourseLecture approvedCourseLecture = new ApprovedCourseLecture();
                            BeanUtils.copyProperties(pendLec, approvedCourseLecture);
                            approvedCourseLecture.setId(null);
                            approvedCourseLecture.setApprovedCourseSection(approvedCourseSection);
                            approvedCourseLecture.setQuiz(pendLec.getQuiz());
                            approvedCourseLecture.setVideo(pendLec.getVideo());
                            approvedCourseLecture.setUserFile(pendLec.getUserFile());
                            approvedCourseLectureRepository.saveAndFlush(approvedCourseLecture);
                            approvedCourseLectures.add(approvedCourseLecture);
                        });
                    }

                });
            }
            return mapCourseEntityToRequest(approvedCourse, approvedCourseSections, approvedCourseLectures);
        } catch (ReflectiveOperationException e) {
            System.out.println("Course could not be approved! Reason: " + e.getMessage());
        }
        return null;
    }


    public ApprovedCourseDTO mapCourseEntityToRequest(ApprovedCourse approvedCourse) {
        if (approvedCourse == null) return null;

        ApprovedCourseDTO approvedCourseDTO = null;
        try {
            approvedCourseDTO = convertShallow(approvedCourse, ApprovedCourseDTO.class, courseEntityToRequestIgnoreProps);

            // Map ignored properties manually
//            if (pendingCourse.getApprovedCourse() != null) {
//                if (pendingCourse.getApprovedCourse().getId() > 0) {
//                    pendingCourseDTO.setApprovedCourseId(pendingCourse.getApprovedCourse().getId());
//                }
//            }

            if (approvedCourse.getCourseCategory() != null) {
                if (approvedCourse.getCourseCategory().getId() > 0) {
                    approvedCourseDTO.setCourseCategoryId(approvedCourse.getCourseCategory().getId());
                }
            }

            try {
                if (approvedCourse.getPromoVideo() == null) throw new RuntimeException();
                VideoRequestDTO video = videoService.mapVideoEntityToRequest(approvedCourse.getPromoVideo());
                approvedCourseDTO.setPromoVideo(video);
            } catch (Exception ignore) {
                approvedCourseDTO.setPromoVideo(null);
            }

            try {
                if (approvedCourse.getThumbnail() == null) throw new RuntimeException();
                UserFileRequestDTO thumbnailFile = userFileService.mapUserFileEntityToRequest(approvedCourse.getThumbnail());
                if (thumbnailFile == null) throw new RuntimeException();
                approvedCourseDTO.setThumbnail(thumbnailFile);
            } catch (Exception ignore) {
                approvedCourseDTO.setThumbnail(null);
            }

            List<ApprovedCourseSection> courseSections = approvedCourseSectionRepository.findAllByApprovedCourse_Id(approvedCourse.getId());
            if (!courseSections.isEmpty()) {
                List<ApprovedCourseSectionDTO> approvedCourseSectionDTOS = courseSections.stream()
                        .map(this::mapCourseSectionEntityToRequest).collect(Collectors.toList());
                approvedCourseDTO.setSections(approvedCourseSectionDTOS);
            }
        }catch(ReflectiveOperationException ignore){}
        return approvedCourseDTO;
    }

    public ApprovedCourseSectionDTO mapCourseSectionEntityToRequest(ApprovedCourseSection approvedCourseSection) {
        ApprovedCourseSectionDTO approvedCourseSectionDTO = null;
        try {
            approvedCourseSectionDTO = convertShallow(approvedCourseSection, ApprovedCourseSectionDTO.class, courseSectionEntityToRequestIgnoreProps);

            // Find the linked lectures and map them to CourseLectureRequest
            List<ApprovedCourseLectureDTO> approvedCourseLectureDTOS = approvedCourseLectureRepository.
                    findAllByApprovedCourseSection_Id(approvedCourseSection.getId())
                    .stream().map(this::mapCourseLectureEntityToRequest).collect(Collectors.toList());
            if (!approvedCourseLectureDTOS.isEmpty()) approvedCourseSectionDTO.setLectures(approvedCourseLectureDTOS);
        }catch(ReflectiveOperationException ignore){}

        return approvedCourseSectionDTO;
    }


    public ApprovedCourseLectureDTO mapCourseLectureEntityToRequest(ApprovedCourseLecture approvedCourseLecture) {
        try {
            if (approvedCourseLecture == null) throw new RuntimeException();
            // Even though courseSection does not exist in the courseLectures, it does in
            ApprovedCourseLectureDTO approvedCourseLectureDTO = convertShallow(approvedCourseLecture, ApprovedCourseLectureDTO.class,
                    courseLectureEntityToRequestIgnoreProps);
            // Map ignored properties manually
            if (approvedCourseLecture.getVideo() != null)
                approvedCourseLectureDTO.setVideo(videoService.mapVideoEntityToRequest(approvedCourseLecture.getVideo()));
            if (approvedCourseLecture.getUserFile() != null)
                approvedCourseLectureDTO.setUserFile(userFileService.mapUserFileEntityToRequest(approvedCourseLecture.getUserFile()));
            if(approvedCourseLecture.getQuiz() != null)
                approvedCourseLectureDTO.setQuiz(quizService.mapQuizToRequest(approvedCourseLecture.getQuiz()));
            return approvedCourseLectureDTO;
        } catch (Exception ignore) {
        }

        return null;
    }


    @SneakyThrows
    public ApprovedCourseDTO mapCourseEntityToRequest(ApprovedCourse approvedCourse,
                                                      List<ApprovedCourseSection> approvedCourseSections,
                                                      List<ApprovedCourseLecture> courseLectures) {
        if (approvedCourse == null) return null;
        ApprovedCourseDTO approvedCourseDTO = null;
        approvedCourseDTO = convertShallow(approvedCourse, ApprovedCourseDTO.class, courseEntityToRequestIgnoreProps);

        if (approvedCourse.getCourseCategory() != null) {
            if (approvedCourse.getCourseCategory().getId() > 0) {
                approvedCourseDTO.setCourseCategoryId(approvedCourse.getCourseCategory().getId());
            }
        }
        List<ApprovedCourseSectionDTO> approvedCourseSectionDTOS = new ArrayList<>();
        approvedCourseSections.forEach(approvedCourseSection -> {
            ApprovedCourseSectionDTO approvedCourseSectionDTO = new ApprovedCourseSectionDTO();
            BeanUtils.copyProperties(approvedCourseSection, approvedCourseSectionDTO);

            List<ApprovedCourseLectureDTO> lectureDTOS = new ArrayList<>();
            if (!CollectionUtils.isEmpty(courseLectures)) {
                courseLectures.stream().filter(lec -> lec.getApprovedCourseSection().getId() == approvedCourseSection.getId())
                        .forEach(approvedCourseLectures -> {
                            ApprovedCourseLectureDTO approvedCourseLectureDTO = new ApprovedCourseLectureDTO();
                            BeanUtils.copyProperties(approvedCourseLectures, approvedCourseLectureDTO);
                            lectureDTOS.add(approvedCourseLectureDTO);
                        });
                approvedCourseSectionDTO.setLectures(lectureDTOS);
            }
            approvedCourseSectionDTOS.add(approvedCourseSectionDTO);
        });

        approvedCourseDTO.setSections(approvedCourseSectionDTOS);
        return approvedCourseDTO;
    }
}
