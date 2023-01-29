package com.loam.stoody.service.product;

import com.loam.stoody.dto.api.request.CourseLectureRequestDTO;
import com.loam.stoody.dto.api.request.CourseOverviewRequestDTO;
import com.loam.stoody.dto.api.request.CourseRequestDTO;
import com.loam.stoody.dto.api.request.CourseSectionRequestDTO;
import com.loam.stoody.dto.api.response.CourseLectureResponseDTO;
import com.loam.stoody.dto.api.response.CourseResponseDTO;
import com.loam.stoody.dto.api.response.CourseSectionResponseDTO;
import com.loam.stoody.enums.CourseStatus;
import com.loam.stoody.model.communication.misc.Comment;
import com.loam.stoody.model.product.course.*;
import com.loam.stoody.model.user.User;
import com.loam.stoody.model.user.courses.PurchasedCourse;
import com.loam.stoody.repository.product.*;
import com.loam.stoody.service.user.CustomUserDetailsService;
import com.loam.stoody.service.user.UserDTS;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CourseService {
    private final CustomUserDetailsService customUserDetailsService;
    private final CourseRepository courseRepository;
    private final CourseSectionRepository courseSectionRepository;
    private final CourseLectureRepository courseLectureRepository;
    private final CommentRepository commentRepository;
    private final CourseRatingRepository courseRatingRepository;
    private final PurchasedCourseRepository purchasedCourseRepository;
    private final UserDTS userDTS;

    public List<CourseResponseDTO> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        List<CourseResponseDTO> courseResponseDTOS = Collections.EMPTY_LIST;
        if (!CollectionUtils.isEmpty(courses)) {
            courseResponseDTOS = courses.stream().map(course -> new CourseResponseDTO(course, true)).collect(Collectors.toList());
        }
        return courseResponseDTOS;
    }

    public CourseResponseDTO getCourseById(Long id) {
        Course course = courseRepository.findById(id).orElse(null);
        if (course != null) return new CourseResponseDTO(course, true);
        return null;
    }

    public Course getCourseEntityById(Long id) {
        return courseRepository.findById(id).orElse(null);
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
    public Course getBlankCourse(){
        Course course = new Course();
        course.setTitle("Untitled");
        course.setAuthor(customUserDetailsService.getCurrentUser());

        course.setIsDeleted(false);
        course.setCreatedBy(customUserDetailsService.getCurrentUser().getId());
        course.setCreatedDate(LocalDateTime.now());
        course.setModifiedBy(customUserDetailsService.getCurrentUser().getId());
        course.setModifiedDate(LocalDateTime.now());
        return course;
    }

    @Transactional
    public Long save(CourseRequestDTO courseRequestDTO) {
        Course course = saveCourse(courseRequestDTO);
        return course.getId();
    }

    @Transactional
    public Long saveEntity(Course course){
        if(course.getId() == null) {
            course.setCreatedDate(LocalDateTime.now());
            course.setCreatedBy(customUserDetailsService.getCurrentUser().getId());
        }
        course.setIsDeleted(false);
        course.setModifiedDate(LocalDateTime.now());
        course.setModifiedBy(customUserDetailsService.getCurrentUser().getId());

        courseRepository.save(course);
        return course.getId();
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
    public CourseRating addRating(long courseId, long rating, long authorId) {
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
    public PurchasedCourse purchaseCourse(long courseId, long userId) {
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
        courseLectureRepository.deleteByIdSectionId(id);
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

    public Optional<CourseResponseDTO> getCourseWithDetails(long id) throws RuntimeException {
        Course course = courseRepository.findById(id).orElseThrow(RuntimeException::new);
        CourseResponseDTO courseResponseDTO = new CourseResponseDTO(course, true);
        List<CourseSection> courseSections = courseSectionRepository.findAllByCourse_Id(id);
        if (!CollectionUtils.isEmpty(courseSections)) {
            courseResponseDTO.setSections(courseSections.stream().map(courseSection -> {
                CourseSectionResponseDTO courseSectionResponseDTO = new CourseSectionResponseDTO(courseSection);
                List<CourseLecture> courseLectures = courseLectureRepository.findAllByCourseSection_Id(courseSection.getId());
                if (!CollectionUtils.isEmpty(courseLectures)) {
                    courseSectionResponseDTO.setLectures(courseLectures.stream().map(courseLecture -> {
                        CourseLectureResponseDTO courseLectureResponseDTO = new CourseLectureResponseDTO(courseLecture);
                        return courseLectureResponseDTO;
                    }).collect(Collectors.toList()));
                }
                return courseSectionResponseDTO;
            }).collect(Collectors.toList()));
        }
        return Optional.ofNullable(courseResponseDTO);
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
            lectureResponseDTOS = courseLectures.stream().map(courseLecture -> new CourseLectureResponseDTO(courseLecture)).collect(Collectors.toList());
        }
        return lectureResponseDTOS;
    }


    public Course saveCourse(CourseRequestDTO courseRequestDTO) {
        Course course = new Course();
        //adding properties for ignoring while update the course
        String[] ignoreProps = new String[]{"viewCount", "uploadDate", "enrolledStudents", "rating"};
        if (null != courseRequestDTO.getId()) {
            course = courseRepository.findById(courseRequestDTO.getId()).orElse(new Course());
            course.setModifiedDate(LocalDateTime.now());
            course.setModifiedBy(customUserDetailsService.getCurrentUser().getId());
        } else {
            ignoreProps = null;
            course.setCreatedDate(LocalDateTime.now());
            course.setCreatedBy(customUserDetailsService.getCurrentUser().getId());
        }
        //setting course category
        CourseCategory courseCategory = new CourseCategory();
        courseCategory.setId(courseRequestDTO.getCourseCategoryId());
        course.setCourseCategory(courseCategory);
        BeanUtils.copyProperties(courseRequestDTO, course, ignoreProps);
        course.setCourseStatus(CourseStatus.Draft);
        course.setIsDeleted(false);
        //getting current login user
        User user = customUserDetailsService.getCurrentUser();
        if (null == user) user = customUserDetailsService.getUserByUsername("Stoody");
        if (null != user) course.setAuthor(user);
        courseRepository.save(course);
        //save course sections
        saveSections(courseRequestDTO.getSections(), course);
        return course;
    }

    public void saveSections(List<CourseSectionRequestDTO> courseSectionRequestDTOS, Course course) {
        courseLectureRepository.deleteAllLectureByCourseId(course.getId());
        courseSectionRepository.deleteAllByCourse_Id(course.getId());
        if (CollectionUtils.isEmpty(courseSectionRequestDTOS)) return;
        courseSectionRequestDTOS.stream().forEach(sectionRequestDTO -> {
            CourseSection courseSection = new CourseSection();
            BeanUtils.copyProperties(sectionRequestDTO, courseSection);
            courseSection.setCourse(course);
            courseSectionRepository.save(courseSection);
            //saving course lectures
            saveLectures(sectionRequestDTO.getLectures(), courseSection);
        });
    }

    public void saveLectures(List<CourseLectureRequestDTO> courseLectureRequestDTOS, CourseSection courseSection) {
        if (CollectionUtils.isEmpty(courseLectureRequestDTOS)) return;
        courseLectureRequestDTOS.stream().forEach(lectureRequestDTO -> {
            CourseLecture courseLecture = new CourseLecture();
            BeanUtils.copyProperties(lectureRequestDTO, courseLecture);
            courseLecture.setCourseSection(courseSection);
            courseLectureRepository.save(courseLecture);
        });
    }

    public void setCourseStatusById(Long id, CourseStatus status){
        Course course = courseRepository.findById(id).orElse(null);
        if(course == null)
            return;
        course.setCourseStatus(status);
        courseRepository.save(course);
    }

    public List<CourseOverviewRequestDTO> getAllCoursesOverview() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        List<CourseOverviewRequestDTO> courseOverviewRequestDTO = new ArrayList<>();
        courseRepository.findAll().forEach(e-> {
            CourseOverviewRequestDTO courseOverviewRequestDTO1 = new CourseOverviewRequestDTO();
            courseOverviewRequestDTO1.setId(e.getId());
            courseOverviewRequestDTO1.setName(e.getTitle());
            courseOverviewRequestDTO1.setCourseThumbnail(e.getThumbnailURL());
            courseOverviewRequestDTO1.setAuthorUsername(e.getAuthor().getUsername());
            courseOverviewRequestDTO1.setStatus(e.getCourseStatus().toString());
            courseOverviewRequestDTO1.setCreatedAt(e.getCreatedDate().format(formatter));
            courseOverviewRequestDTO.add(courseOverviewRequestDTO1);
        });
        return courseOverviewRequestDTO;
    }

    public String getCourseCreationDateString(Long id){
        Course course = getCourseEntityById(id);
        if(course == null)
            return null;
        if(course.getCreatedDate() == null)
            return null;
        return course.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public List<Course> searchCourseEntityByTitle(String searchKey){
        return courseRepository.findAll().stream().filter(e->e.getTitle().toLowerCase().equals(searchKey.toLowerCase())).toList();
    }

    // User Specific
    public List<Course> getCurrentUserCourseEntities(){
        return courseRepository.findAll().stream().filter(e->e.getAuthor().getUsername().equals(customUserDetailsService.getCurrentUser().getUsername())).toList();
    }
}