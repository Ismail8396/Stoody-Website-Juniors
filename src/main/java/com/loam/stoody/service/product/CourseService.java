package com.loam.stoody.service.product;

import com.loam.stoody.dto.api.response.CourseResponseDTO;
import com.loam.stoody.enums.CourseStatus;
import com.loam.stoody.model.product.course.Course;
import com.loam.stoody.repository.product.CourseLectureRepository;
import com.loam.stoody.repository.product.CourseRepository;
import com.loam.stoody.repository.product.CourseSectionRepository;
import com.loam.stoody.service.utils.aws.S3Service;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final CourseSectionRepository courseSectionRepository;
    private final CourseLectureRepository courseLectureRepository;
    private final S3Service s3Service;

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public List<Course> getAllCoursesByCategoryId(long id) {
        return courseRepository.findAllByCourseCategory(id);
    }

    @Transactional
    public Course save(Course course) {
        course.setCourseStatus(CourseStatus.Draft);
        return courseRepository.save(course);
    }

    @Transactional
    public void delete(long id) {
        courseRepository.deleteById(id);
    }

    @Transactional
    public void deleteSection(long id) {
        courseSectionRepository.deleteById(id);
    }

    @Transactional
    public void deleteLecture(long id) {
        courseLectureRepository.deleteById(id);
    }

    public Optional<CourseResponseDTO> getCourseById(long id) {
        Course course = courseRepository.findById(id).orElseThrow(RuntimeException::new);
        return  Optional.ofNullable(new CourseResponseDTO(course));
    }
}
