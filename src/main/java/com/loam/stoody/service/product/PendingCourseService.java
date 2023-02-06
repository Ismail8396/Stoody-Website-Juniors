package com.loam.stoody.service.product;

import com.loam.stoody.dto.api.request.course.*;
import com.loam.stoody.dto.api.request.course.pending.PendingCourseDTO;
import com.loam.stoody.dto.api.request.course.pending.PendingCourseLectureDTO;
import com.loam.stoody.dto.api.request.course.pending.PendingCourseSectionDTO;
import com.loam.stoody.dto.api.request.file.UserFileRequestDTO;
import com.loam.stoody.global.constants.MiscConstants;
import com.loam.stoody.global.constants.ProjectConfigurationVariables;
import com.loam.stoody.model.communication.file.UserFile;
import com.loam.stoody.model.product.course.pending.PendingCourse;
import com.loam.stoody.model.product.course.pending.PendingCourseLecture;
import com.loam.stoody.model.product.course.pending.PendingCourseSection;
import com.loam.stoody.repository.product.*;
import com.loam.stoody.repository.product.approved.ApprovedCourseRepository;
import com.loam.stoody.repository.product.pending.PendingCourseLectureRepository;
import com.loam.stoody.repository.product.pending.PendingCourseRepository;
import com.loam.stoody.repository.product.pending.PendingCourseSectionRepository;
import com.loam.stoody.service.communication.file.UserFileService;
import com.loam.stoody.service.communication.video.VideoService;
//import com.loam.stoody.service.product.quiz.QuizService;
import com.loam.stoody.service.user.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PendingCourseService extends CourseCoreService {
    private final CustomUserDetailsService customUserDetailsService;
    private final UserFileService userFileService;
    private final VideoService videoService;

    private final CourseCategoryRepository categoryRepository;

    private final ApprovedCourseRepository approvedCourseRepository;

    private final PendingCourseRepository pendingCourseRepository;
    private final PendingCourseLectureRepository courseLectureRepository;
    private final PendingCourseSectionRepository courseSectionRepository;

    //private final QuizService quizService;

    public PendingCourseLectureDTO mapCourseLectureEntityToRequest(PendingCourseLecture pendingCourseLecture) {
        try {
            if (pendingCourseLecture == null) throw new RuntimeException();
            // Even though courseSection does not exist in the courseLectures, it does in
            PendingCourseLectureDTO pendingCourseLectureDTO = convertShallow(pendingCourseLecture, PendingCourseLectureDTO.class, courseLectureEntityToRequestIgnoreProps);
            // Map ignored properties manually
            if (pendingCourseLecture.getVideo() != null)
                pendingCourseLectureDTO.setVideo(videoService.mapVideoEntityToRequest(pendingCourseLecture.getVideo()));
            if (pendingCourseLecture.getUserFile() != null)
                pendingCourseLectureDTO.setUserFile(userFileService.mapUserFileEntityToRequest(pendingCourseLecture.getUserFile()));
            return pendingCourseLectureDTO;
        } catch (Exception ignore) {
        }

        return null;
    }

    public PendingCourseLecture mapCourseLectureRequestToEntity(PendingCourseLectureDTO pendingCourseLectureDTO, PendingCourseSection pendingCourseSection) {
        if (pendingCourseLectureDTO == null) return null;
        PendingCourseLecture pendingCourseLecture = null;
        try {

            pendingCourseLecture = convertShallow(pendingCourseLectureDTO, PendingCourseLecture.class, courseLectureRequestToEntityIgnoreProps);

            // Map ignored properties manually
            pendingCourseLecture.setPendingCourseSection(pendingCourseSection);

            if (pendingCourseLectureDTO.getVideo() != null) {
                try {
                    pendingCourseLecture.setVideo(videoService.mapVideoRequestToEntity(pendingCourseLectureDTO.getVideo()));
                } catch (Exception ignore) {
                }
            }

            if (pendingCourseLectureDTO.getUserFile() != null) {
                try {
                    pendingCourseLecture.setUserFile(userFileService.mapUserFileRequestToEntity(pendingCourseLectureDTO.getUserFile()));
                } catch (Exception ignore) {
                }
            }
        }catch(ReflectiveOperationException ignore){}
        return pendingCourseLecture;
    }

    public PendingCourseSection mapCourseSectionRequestToEntity(PendingCourseSectionDTO pendingCourseSectionDTO, PendingCourse pendingCourse) {
        PendingCourseSection pendingCourseSection = null;
        try {
            pendingCourseSection = convertShallow(pendingCourseSectionDTO, PendingCourseSection.class, courseSectionRequestToEntityIgnoreProps);
            pendingCourseSection.setPendingCourse(pendingCourseRepository.findById(pendingCourse.getId()).orElse(null));
        }catch(ReflectiveOperationException ignore){
        }
        return pendingCourseSection;
    }

    public PendingCourseSectionDTO mapCourseSectionEntityToRequest(PendingCourseSection pendingCourseSection) {
        PendingCourseSectionDTO pendingCourseSectionDTO = null;
        try {
            pendingCourseSectionDTO = convertShallow(pendingCourseSection, PendingCourseSectionDTO.class, courseSectionEntityToRequestIgnoreProps);

            // Find the linked lectures and map them to CourseLectureRequest
            List<PendingCourseLectureDTO> sectionLectures = courseLectureRepository.findAll().stream().filter(i -> i.getPendingCourseSection() != null && i.getPendingCourseSection().getId().equals(pendingCourseSection.getId())).map(this::mapCourseLectureEntityToRequest).collect(Collectors.toList());
            if (!sectionLectures.isEmpty()) pendingCourseSectionDTO.setLectures(sectionLectures);
        }catch(ReflectiveOperationException ignore){}

        return pendingCourseSectionDTO;
    }

    public PendingCourse mapCourseRequestToEntity(PendingCourseDTO courseRequest) {
        if (courseRequest == null) return null;

        PendingCourse courseEntityChecker;
        if (courseRequest.getId() != null && courseRequest.getId() > 0) {
            courseEntityChecker = pendingCourseRepository.findById(courseRequest.getId()).orElse(null);
            if (courseEntityChecker == null) courseEntityChecker = new PendingCourse();
        } else {
            courseEntityChecker = new PendingCourse();
        }

        PendingCourse courseEntity = courseEntityChecker;

        final String[] ignoreProps = new String[]{"promoVideo", "sections", "thumbnail", "courseCategoryId", "author", "approvedCourseId"};
        BeanUtils.copyProperties(courseRequest, courseEntity, ignoreProps);

        courseEntity.setIsDeleted(false);

        final Long approvedCourseId = courseRequest.getApprovedCourseId();
        if(approvedCourseId != null){
            if(approvedCourseId > 0){
                approvedCourseRepository.findById(approvedCourseId).ifPresent(courseEntity::setApprovedCourse);
            }
        }

        final Integer courseCategoryId = courseRequest.getCourseCategoryId();
        if(courseCategoryId != null){
            if(courseCategoryId > 0){
                categoryRepository.findById(courseCategoryId).ifPresent(courseEntity::setCourseCategory);
            }
        }

        try {
            courseEntity.setPromoVideo(videoService.mapVideoRequestToEntity(courseRequest.getPromoVideo()));
        } catch (Exception ignore) {
        }

        UserFile thumbnailFile = userFileService.mapUserFileRequestToEntity(courseRequest.getThumbnail());
        courseEntity.setThumbnail(thumbnailFile);

        // Important note:
        // PendingCourse Sections and Lectures within them are separate entities.
        // So, we have to serialize/deserialize them separately.
        // They have their own save method.
        // See: saveCourseSections(), saveCourseLectures()

        return courseEntity;
    }

    public PendingCourseDTO mapCourseEntityToRequest(PendingCourse pendingCourse) {
        if (pendingCourse == null) return null;

        PendingCourseDTO pendingCourseDTO = null;
        try {
            pendingCourseDTO = convertShallow(pendingCourse, PendingCourseDTO.class, courseEntityToRequestIgnoreProps);

            // Map ignored properties manually
            if (pendingCourse.getApprovedCourse() != null) {
                if (pendingCourse.getApprovedCourse().getId() > 0) {
                    pendingCourseDTO.setApprovedCourseId(pendingCourse.getApprovedCourse().getId());
                }
            }

            if (pendingCourse.getCourseCategory() != null) {
                if (pendingCourse.getCourseCategory().getId() > 0) {
                    pendingCourseDTO.setCourseCategoryId(pendingCourse.getCourseCategory().getId());
                }
            }

            try {
                if (pendingCourse.getPromoVideo() == null) throw new RuntimeException();
                VideoRequestDTO video = videoService.mapVideoEntityToRequest(pendingCourse.getPromoVideo());
                pendingCourseDTO.setPromoVideo(video);
            } catch (Exception ignore) {
                pendingCourseDTO.setPromoVideo(null);
            }

            try {
                if (pendingCourse.getThumbnail() == null) throw new RuntimeException();
                UserFileRequestDTO thumbnailFile = userFileService.mapUserFileEntityToRequest(pendingCourse.getThumbnail());
                if (thumbnailFile == null) throw new RuntimeException();
                pendingCourseDTO.setThumbnail(thumbnailFile);
            } catch (Exception ignore) {
                pendingCourseDTO.setThumbnail(null);
            }

            List<PendingCourseSection> courseSections = courseSectionRepository.findAll().stream().filter(e -> e.getPendingCourse().getId().equals(pendingCourse.getId())).toList();
            if (!courseSections.isEmpty()) {
                List<PendingCourseSectionDTO> pendingCourseSectionDTOS = courseSections.stream().map(this::mapCourseSectionEntityToRequest).collect(Collectors.toList());
                pendingCourseDTO.setSections(pendingCourseSectionDTOS);
            }
        }catch(ReflectiveOperationException ignore){}
        return pendingCourseDTO;
    }

    @Transactional
    public PendingCourse savePendingCourse(PendingCourse pendingCourse){
        return pendingCourseRepository.save(pendingCourse);
    }

    @Transactional
    public PendingCourseDTO savePendingCourse(PendingCourseDTO courseRequest) {
        PendingCourse courseEntity = mapCourseRequestToEntity(courseRequest);

        // Modified By/Date & Created By/Date
        if(courseRequest.getId() != null && courseRequest.getId() > 0){
            if(pendingCourseRepository.findById(courseRequest.getId()).isPresent()){
                courseEntity.setModifiedDate(LocalDateTime.now());
                if(ProjectConfigurationVariables.stoodyEnvironment.equals(ProjectConfigurationVariables.developmentMode)){
                    if(customUserDetailsService.getCurrentUser() != null)
                        courseEntity.setModifiedBy(customUserDetailsService.getCurrentUser().getId());
                }else{
                    if(customUserDetailsService.getCurrentUser() == null)
                        return null;
                    courseEntity.setModifiedBy(customUserDetailsService.getCurrentUser().getId());
                }
            }
        }else{
            courseEntity.setCreatedDate(LocalDateTime.now());

            if(ProjectConfigurationVariables.stoodyEnvironment.equals(ProjectConfigurationVariables.developmentMode)){
                if(customUserDetailsService.getCurrentUser() != null)
                    courseEntity.setCreatedBy(customUserDetailsService.getCurrentUser().getId());
            }else{
                if(customUserDetailsService.getCurrentUser() == null)
                    return null;
                courseEntity.setCreatedBy(customUserDetailsService.getCurrentUser().getId());
            }
        }

        // Author
        if(ProjectConfigurationVariables.stoodyEnvironment.equals(ProjectConfigurationVariables.developmentMode)){
            courseEntity.setAuthor(customUserDetailsService.getUserByUsername("OrkhanGG"));
        }else{
            if(customUserDetailsService.getCurrentUser() == null)
                return null;
            courseEntity.setAuthor(customUserDetailsService.getCurrentUser());
        }

        // Save course and assign the saved entity object to the courseEntity instance
        courseEntity = pendingCourseRepository.save(courseEntity);

        // Remove all sections and lectures belong to this course if there's any
        // PendingCourse Lectures should be deleted first!
        courseLectureRepository.removeAllByCourseId(courseEntity.getId());
        courseSectionRepository.removeAllByCourseId(courseEntity.getId());

        // Add valid sections and lectures.
        // TODO: Using stream() will be better.
        if (courseRequest.getSections() != null) {
            for (var i : courseRequest.getSections()) {
                PendingCourseSection pendingCourseSection = mapCourseSectionRequestToEntity(i, courseEntity);
                if (pendingCourseSection != null) {
                    courseSectionRepository.save(pendingCourseSection);
                    if (i.getLectures() != null) {
                        for (var j : i.getLectures()) {
                            PendingCourseLecture pendingCourseLecture = mapCourseLectureRequestToEntity(j, pendingCourseSection);
                            if (pendingCourseLecture != null) {
                                courseLectureRepository.save(pendingCourseLecture);
                            }
                        }
                    }
                }
            }
        }

        return mapCourseEntityToRequest(courseEntity);
    }

    public List<PendingCourseDTO> getAllPendingCourses() {
        return pendingCourseRepository.findAll().stream().map(this::mapCourseEntityToRequest).collect(Collectors.toList());
    }

    public <R> R getPendingCourseById(Long id, Class<R> responseType) {
        PendingCourse pendingCourse = pendingCourseRepository.findById(id).orElse(null);
        if(responseType.equals(PendingCourseDTO.class)) {
            if (pendingCourse == null)
                return null;
            return responseType.cast(mapCourseEntityToRequest(pendingCourse));
        } else if(responseType.equals(PendingCourse.class)) {
            return responseType.cast(pendingCourse);
        }
        return null;
    }

    public <R> List<R> getCurrentUserPendingCourses(Class<R> responseType) {
        String username = customUserDetailsService.getCurrentUser().getUsername();
        final List<PendingCourse> pendingCourses = pendingCourseRepository.findAll().stream()
                .filter(e -> e.getAuthor() != null && e.getAuthor().getUsername().equals(username))
                .collect(Collectors.toList());
        if (pendingCourses.isEmpty()) {
            return Collections.emptyList();
        }

        return pendingCourses.stream()
                .map(course -> responseType.equals(PendingCourseDTO.class)
                        ? responseType.cast(mapCourseEntityToRequest(course))
                        : responseType.cast(course))
                .collect(Collectors.toList());
    }


    public String getCourseCreationDateByIdFormatted(Long id){
        PendingCourse course = pendingCourseRepository.findById(id).orElse(null);
        if(course == null)
            return LocalDateTime.now().format(MiscConstants.standardDateTimeFormat);
        if(course.getCreatedDate() == null)
            return null;
        return course.getCreatedDate().format(MiscConstants.standardDateTimeFormat);
    }

    public Long getEnrollmentCount(Long id){
        return 0L;
    }

    public Double getPendingCourseRating(Long id){
        return 0D;
    }

//    public List<CourseResponseDTO> getAllCourses() {
////        List<PendingCourse> courses = courseRepository.findAll();
////        List<CourseResponseDTO> courseResponseDTOS = Collections.EMPTY_LIST;
////        if (!CollectionUtils.isEmpty(courses)) {
////            courseResponseDTOS = courses.stream().map(course -> new CourseResponseDTO(course, true)).collect(Collectors.toList());
////        }
////        return courseResponseDTOS;
//        return null;
//    }
//
//    public CourseResponseDTO getCourseById(Long id) {
////        PendingCourse course = courseRepository.findById(id).orElse(null);
////        if (course != null) return new CourseResponseDTO(course, true);
//        return null;
//    }
//
//    public PendingCourse getCourseEntityById(Long id) {
//        return courseRepository.findById(id).orElse(null);
//    }
//
//    public List<CourseResponseDTO> getAllCoursesByCategoryId(long id) {
////        List<PendingCourse> courses = courseRepository.findAllByCourseCategory_Id(id);
////        List<CourseResponseDTO> courseResponseDTOS = Collections.EMPTY_LIST;
////        if (!CollectionUtils.isEmpty(courses)) {
////            courseResponseDTOS = courses.stream().map(course -> new CourseResponseDTO(course, true)).collect(Collectors.toList());
////        }
////        return courseResponseDTOS;
//        return null;
//    }
//
//    @Transactional
//    public PendingCourse getBlankCourse(){
//        PendingCourse course = new PendingCourse();
//        course.setTitle("Untitled");
//        course.setAuthor(customUserDetailsService.getCurrentUser());
//
//        course.setIsDeleted(false);
//        course.setCreatedBy(customUserDetailsService.getCurrentUser().getId());
//        course.setCreatedDate(LocalDateTime.now());
//        course.setModifiedBy(customUserDetailsService.getCurrentUser().getId());
//        course.setModifiedDate(LocalDateTime.now());
//        return course;
//    }
//
//    @Transactional
//    public Long save(PendingCourseDTO courseRequestDTO) {
//        PendingCourse course = saveCourse(courseRequestDTO);
//        return course.getId();
//    }
//
//    @Transactional
//    public Long saveEntity(PendingCourse course){
//        if(course.getId() == null) {
//            course.setCreatedDate(LocalDateTime.now());
//            course.setCreatedBy(customUserDetailsService.getCurrentUser().getId());
//        }
//        course.setIsDeleted(false);
//        course.setModifiedDate(LocalDateTime.now());
//        course.setModifiedBy(customUserDetailsService.getCurrentUser().getId());
//
//        courseRepository.save(course);
//        return course.getId();
//    }
//
//    @Transactional
//    public Comment addComment(long courseId, String content, long authorId) {
//        Comment comment = new Comment();
//
//        PendingCourse course = new PendingCourse();
//        course.setId(courseId);
//
//        User user = new User();
//        user.setId(authorId);
//
//        comment.setPendingCourse(course);
//        comment.setContent(content);
//        comment.setAuthor(user);
//        return commentRepository.save(comment);
//    }
//
//    @Transactional
//    public CourseRating addRating(long courseId, long rating, long authorId) {
//        CourseRating courseRating = new CourseRating();
//
//        PendingCourse course = new PendingCourse();
//        course.setId(courseId);
//
//        User user = new User();
//        user.setId(authorId);
//
//        courseRating.setPendingCourse(course);
//        courseRating.setRating(rating);
//        courseRating.setAuthor(user);
//        return courseRatingRepository.save(courseRating);
//    }
//
//    @Transactional
//    public PurchasedCourse purchaseCourse(long courseId, long userId) {
//        PurchasedCourse purchasedCourse = new PurchasedCourse();
//
//        PendingCourse course = new PendingCourse();
//        course.setId(courseId);
//
//        User user = new User();
//        user.setId(userId);
//
//        purchasedCourse.setPendingCourse(course);
//        purchasedCourse.setUser(user);
//        return purchasedCourseRepository.save(purchasedCourse);
//    }
//
//    @Transactional
//    public void delete(long id) {
//        courseRepository.deleteCourse(id);
//    }
//
//    @Transactional
//    public void deleteSection(long id) {
//        courseLectureRepository.deleteByIdSectionId(id);
//        courseSectionRepository.deleteById(id);
//    }
//
//    @Transactional
//    public void deleteLecture(long id) {
//        courseLectureRepository.deleteById(id);
//    }
//
//    public Optional<CourseResponseDTO> getPendingCourse(long id) {
////        PendingCourse course = courseRepository.findById(id).orElse(new PendingCourse());
////        return Optional.ofNullable(new CourseResponseDTO(course, false));
//        return null;
//    }
//
//    public Optional<CourseResponseDTO> getCourseWithDetails(long id) throws RuntimeException {
////        PendingCourse course = courseRepository.findById(id).orElseThrow(RuntimeException::new);
////        CourseResponseDTO courseResponseDTO = new CourseResponseDTO(course, true);
////        List<PendingCourseSection> courseSections = courseSectionRepository.findAllByCourse_Id(id);
////        if (!CollectionUtils.isEmpty(courseSections)) {
////            courseResponseDTO.setSections(courseSections.stream().map(courseSection -> {
////                CourseSectionResponseDTO courseSectionResponseDTO = new CourseSectionResponseDTO(courseSection);
////                List<PendingCourseLecture> courseLectures = courseLectureRepository.findAllByCourseSection_Id(courseSection.getId());
////                if (!CollectionUtils.isEmpty(courseLectures)) {
////                    courseSectionResponseDTO.setLectures(courseLectures.stream().map(courseLecture -> {
////                        CourseLectureResponseDTO courseLectureResponseDTO = new CourseLectureResponseDTO(courseLecture);
////                        //find all quiz per lecture
////                        quizService.findAllQuizDetails(courseLecture.getId(), courseLectureResponseDTO);
////                        return courseLectureResponseDTO;
////                    }).collect(Collectors.toList()));
////                }
////                return courseSectionResponseDTO;
////            }).collect(Collectors.toList()));
////        }
////        return Optional.ofNullable(courseResponseDTO);
//        return null;
//    }
//
//    public Optional<CourseResponseDTO> getCourseRestricted(long id) throws RuntimeException {
////        PendingCourse course = courseRepository.findById(id).orElseThrow(RuntimeException::new);
////        CourseResponseDTO courseResponseDTO = new CourseResponseDTO(course, true);
////        List<PendingCourseSection> courseSections = courseSectionRepository.findAllByCourse_Id(id);
////        if (!CollectionUtils.isEmpty(courseSections)) {
////            courseResponseDTO.setSections(courseSections.stream().map(courseSection -> {
////                CourseSectionResponseDTO courseSectionResponseDTO = new CourseSectionResponseDTO(courseSection);
////                List<PendingCourseLecture> courseLectures = courseLectureRepository.findAllByCourseSection_Id(courseSection.getId());
////                if (!CollectionUtils.isEmpty(courseLectures)) {
////                    courseSectionResponseDTO.setLectures(courseLectures.stream().map(courseLecture -> {
////                        CourseLectureResponseDTO courseLectureResponseDTO = new CourseLectureResponseDTO(courseLecture);
////                        //find all quiz per lecture
////                        quizService.findAllQuizDetails(courseLecture.getId(), courseLectureResponseDTO);
////                        return courseLectureResponseDTO;
////                    }).collect(Collectors.toList()));
////                }
////                return courseSectionResponseDTO;
////            }).collect(Collectors.toList()));
////        }
////        return Optional.ofNullable(courseResponseDTO);
//        return null;
//    }
//
//    public List<CourseSectionResponseDTO> getSectionsByCourseId(long courseId) {
//        List<PendingCourseSection> courseSections = courseSectionRepository.findAllByCourse_Id(courseId);
//        List<CourseSectionResponseDTO> sectionsDtos = Collections.EMPTY_LIST;
//        if (!CollectionUtils.isEmpty(courseSections)) {
//            sectionsDtos = courseSections.stream().map(courseSection -> new CourseSectionResponseDTO(courseSection)).collect(Collectors.toList());
//        }
//        return sectionsDtos;
//    }
//
//    public List<CourseLectureResponseDTO> findLecturesBySectionId(long sectionId) {
//        List<PendingCourseLecture> courseLectures = courseLectureRepository.findAllByCourseSection_Id(sectionId);
//        List<CourseLectureResponseDTO> lectureResponseDTOS = Collections.EMPTY_LIST;
//        if (!CollectionUtils.isEmpty(courseLectures)) {
//            lectureResponseDTOS = courseLectures.stream().map(courseLecture -> new CourseLectureResponseDTO(courseLecture)).collect(Collectors.toList());
//        }
//        return lectureResponseDTOS;
//    }
//
//
//    public PendingCourse saveCourse(PendingCourseDTO courseRequestDTO) {
//        PendingCourse course = new PendingCourse();
//        //adding properties for ignoring while update the course
//        String[] ignoreProps = new String[]{"viewCount", "uploadDate", "enrolledStudents", "rating"};
//        if (null != courseRequestDTO.getId()) {
//            course = courseRepository.findById(courseRequestDTO.getId()).orElse(new PendingCourse());
//            course.setModifiedDate(LocalDateTime.now());
//            course.setModifiedBy(customUserDetailsService.getCurrentUser().getId());
//        } else {
//            ignoreProps = null;
//            course.setCreatedDate(LocalDateTime.now());
//            course.setCreatedBy(customUserDetailsService.getCurrentUser().getId());
//        }
//        //setting course category
//        CourseCategory courseCategory = new CourseCategory();
//        courseCategory.setId(courseRequestDTO.getCourseCategoryId());
//        course.setCourseCategory(courseCategory);
//        BeanUtils.copyProperties(courseRequestDTO, course, ignoreProps);
//        course.setCourseStatus(CourseStatus.Draft);
//        course.setIsDeleted(false);
//        //getting current login user
//        User user = customUserDetailsService.getCurrentUser();
//        if (null == user) user = customUserDetailsService.getUserByUsername("Stoody");
//        if (null != user) course.setAuthor(user);
//        courseRepository.save(course);
//        //save course sections
//        saveSections(courseRequestDTO.getSections(), course);
//        return course;
//    }
//
//    public void saveSections(List<PendingCourseSectionDTO> courseSectionRequestDTOS, PendingCourse course) {
//        List<PendingCourseLecture> courseLectures = courseLectureRepository.findAllByCourseSection_Course_Id(course.getId());
//        quizService.deleteAllQuiz(courseLectures);
//        courseLectureRepository.removeAllByCourseId(course.getId());
//        courseSectionRepository.removeAllByCourseId(course.getId());
//        if (CollectionUtils.isEmpty(courseSectionRequestDTOS)) return;
//        courseSectionRequestDTOS.stream().forEach(sectionRequestDTO -> {
//            PendingCourseSection courseSection = new PendingCourseSection();
//            BeanUtils.copyProperties(sectionRequestDTO, courseSection);
//            courseSection.setPendingCourse(course);
//            courseSectionRepository.save(courseSection);
//            //saving course lectures
//            saveLectures(sectionRequestDTO.getLectures(), courseSection);
//        });
//    }
//
//    public void saveLectures(List<PendingCourseLectureDTO> courseLectureRequestDTOS, PendingCourseSection courseSection) {
//        if (CollectionUtils.isEmpty(courseLectureRequestDTOS)) return;
//        courseLectureRequestDTOS.stream().forEach(lectureRequestDTO -> {
//            PendingCourseLecture courseLecture = new PendingCourseLecture();
//            BeanUtils.copyProperties(lectureRequestDTO, courseLecture);
//            courseLecture.setPendingCourseSection(courseSection);
//            courseLectureRepository.save(courseLecture);
//            //save lecture quiz
//            quizService.save(lectureRequestDTO.getQuiz(), courseLecture);
//        });
//    }
//
//    public void setCourseStatusById(Long id, CourseStatus status){
//        PendingCourse course = courseRepository.findById(id).orElse(null);
//        if(course == null)
//            return;
//        course.setCourseStatus(status);
//        courseRepository.save(course);
//    }
//
//    public List<CourseOverviewRequestDTO> getAllCoursesOverview() {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
//        List<CourseOverviewRequestDTO> courseOverviewRequestDTO = new ArrayList<>();
//        courseRepository.findAll().forEach(e-> {
//            CourseOverviewRequestDTO courseOverviewRequestDTO1 = new CourseOverviewRequestDTO();
//            courseOverviewRequestDTO1.setId(e.getId());
//            courseOverviewRequestDTO1.setName(e.getTitle());
//            //courseOverviewRequestDTO1.setCourseThumbnail(e.getThumbnailURL());
//            courseOverviewRequestDTO1.setAuthorUsername(e.getAuthor().getUsername());
//            courseOverviewRequestDTO1.setStatus(e.getCourseStatus().toString());
//            courseOverviewRequestDTO1.setCreatedAt(e.getCreatedDate().format(formatter));
//            courseOverviewRequestDTO.add(courseOverviewRequestDTO1);
//        });
//        return courseOverviewRequestDTO;
//    }
//
//    public String getCourseCreationDateString(Long id){
//        PendingCourse course = getCourseEntityById(id);
//        if(course == null)
//            return null;
//        if(course.getCreatedDate() == null)
//            return null;
//        return course.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
//    }
//
//    public List<PendingCourse> searchCourseEntityByTitle(String searchKey){
//        return courseRepository.findAll().stream().filter(e->e.getTitle().toLowerCase().equals(searchKey.toLowerCase())).toList();
//    }
//
//    // User Specific
//    public List<PendingCourse> getCurrentUserCourseEntities(){
//        return courseRepository.findAll().stream().filter(e->e.getAuthor().getUsername().equals(customUserDetailsService.getCurrentUser().getUsername())).toList();
//    }
}