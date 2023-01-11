package com.loam.stoody.service.product;

import com.loam.stoody.dto.api.response.CourseLectureResponseDTO;
import com.loam.stoody.dto.api.response.CourseResponseDTO;
import com.loam.stoody.dto.api.response.CourseSectionResponseDTO;
import com.loam.stoody.enums.CourseStatus;
import com.loam.stoody.global.logger.ConsoleColors;
import com.loam.stoody.global.logger.StoodyLogger;
import com.loam.stoody.model.communication.misc.Comment;
import com.loam.stoody.model.product.course.*;
import com.loam.stoody.model.user.User;
import com.loam.stoody.model.user.courses.PurchasedCourse;
import com.loam.stoody.repository.product.*;
import com.loam.stoody.service.utils.aws.S3Service;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final CourseSectionRepository courseSectionRepository;
    private final CourseLectureRepository courseLectureRepository;
    private final CommentRepository commentRepository;
    private final CourseRatingRepository courseRatingRepository;
    private final PurchasedCourseRepository purchasedCourseRepository;
    private final S3Service s3Service;

    public List<CourseResponseDTO> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        List<CourseResponseDTO> courseResponseDTOS = Collections.EMPTY_LIST;
        if (!CollectionUtils.isEmpty(courses)) {
            courseResponseDTOS = courses.stream().map(course -> new CourseResponseDTO(course, true)).collect(Collectors.toList());
        }
        return courseResponseDTOS;
    }

    public List<CourseResponseDTO> getAllCoursesByCategoryId(long id) {
        List<Course> courses = courseRepository.findAllByCourseCategory_Id(id);
        List<CourseResponseDTO> courseResponseDTOS = Collections.EMPTY_LIST;
        if (!CollectionUtils.isEmpty(courses)) {
            courseResponseDTOS = courses.stream().map(course -> new CourseResponseDTO(course, true)).collect(Collectors.toList());
        }
        return courseResponseDTOS;
    }

    @Transactional
    public Course save(Course course) {
        course.setCourseStatus(CourseStatus.Draft);
        course.setIsDeleted(false);
        return courseRepository.save(course);
    }

    @Transactional
    public Comment addComment(long courseId, String content, long authorId) {
        Comment comment = new Comment();

        Course course = new Course();
        course.setId(courseId);

        User user = new User();
        user.setId(authorId);

        comment.setCourse(course);
        comment.setContent(content);
        comment.setAuthor(user);
        return commentRepository.save(comment);
    }

    @Transactional
    public CourseRating addRatting(long courseId,
                                   long rating,
                                   long authorId) {
        CourseRating courseRating = new CourseRating();

        Course course = new Course();
        course.setId(courseId);

        User user = new User();
        user.setId(authorId);

        courseRating.setCourse(course);
        courseRating.setRating(rating);
        courseRating.setAuthor(user);
        return courseRatingRepository.save(courseRating);
    }

    @Transactional
    public PurchasedCourse purchaseCourse(long courseId,
                                          long userId) {
        PurchasedCourse purchasedCourse = new PurchasedCourse();

        Course course = new Course();
        course.setId(courseId);

        User user = new User();
        user.setId(userId);

        purchasedCourse.setCourse(course);
        purchasedCourse.setUser(user);
        return purchasedCourseRepository.save(purchasedCourse);
    }

    @Transactional
    public void delete(long id) {
        courseRepository.deleteCourse(id);
    }

    @Transactional
    public void deleteSection(long id) {
        courseSectionRepository.deleteById(id);
    }

    @Transactional
    public void deleteLecture(long id) {
        courseLectureRepository.deleteById(id);
    }

    public Optional<CourseResponseDTO> getCourse(long id) {
        Course course = courseRepository.findById(id).orElse(new Course());
        return Optional.ofNullable(new CourseResponseDTO(course, false));
    }

    public Optional<CourseResponseDTO> getCourseWithDetails(long id) {
        Course course = courseRepository.findById(id).orElseThrow(RuntimeException::new);
        return Optional.ofNullable(new CourseResponseDTO(course, true));
    }

    public List<CourseSectionResponseDTO> getSectionsByCourseId(long courseId) {
        List<CourseSection> courseSections = courseSectionRepository.findAllByCourse_Id(courseId);
        List<CourseSectionResponseDTO> sectionsDtos = Collections.EMPTY_LIST;
        if (!CollectionUtils.isEmpty(courseSections)) {
            sectionsDtos = courseSections.stream().map(courseSection -> new CourseSectionResponseDTO(courseSection)).collect(Collectors.toList());
        }
        return sectionsDtos;
    }

    public List<CourseLectureResponseDTO> findLecturesBySectionId(long sectionId) {
        List<CourseLecture> courseLectures = courseLectureRepository.findAllByCourseSection_Id(sectionId);
        List<CourseLectureResponseDTO> lectureResponseDTOS = Collections.EMPTY_LIST;
        if (!CollectionUtils.isEmpty(courseLectures)) {
            lectureResponseDTOS = courseLectures.stream().map(courseLecture
                    -> new CourseLectureResponseDTO(courseLecture)).collect(Collectors.toList());
        }
        return lectureResponseDTOS;
    }
}
