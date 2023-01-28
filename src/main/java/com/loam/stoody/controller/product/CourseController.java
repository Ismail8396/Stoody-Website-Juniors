package com.loam.stoody.controller.product;

import com.loam.stoody.dto.api.request.CourseOverviewRequestDTO;
import com.loam.stoody.dto.api.request.CourseRequestDTO;
import com.loam.stoody.dto.api.response.CourseResponseDTO;
import com.loam.stoody.dto.api.response.OutdoorResponse;
import com.loam.stoody.enums.CourseStatus;
import com.loam.stoody.enums.SimpleNotificationBadge;
import com.loam.stoody.global.annotations.UnderDevelopment;
import com.loam.stoody.global.constants.IndoorResponse;
import com.loam.stoody.global.constants.PRL;
import com.loam.stoody.model.product.course.Course;
import com.loam.stoody.model.user.User;
import com.loam.stoody.repository.product.CourseRepository;
import com.loam.stoody.service.communication.notification.NotificationService;
import com.loam.stoody.service.i18n.LanguageService;
import com.loam.stoody.service.product.CategoryService;
import com.loam.stoody.service.product.CourseService;
import com.loam.stoody.service.user.CustomUserDetailsService;
import com.loam.stoody.service.user.UserDTS;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping(PRL.apiPrefix+PRL.apiCourseSuffixURL)
@AllArgsConstructor
public class CourseController {
    private final NotificationService notificationService;
    private final CustomUserDetailsService customUserDetailsService;
    private final CourseService courseService;
    private final CategoryService categoryService;
    private final LanguageService languageService;
    private final UserDTS userDTS;
    private final CourseRepository courseRepository;

    @ModelAttribute("getUserDTS")
    public UserDTS getUserDTS() {
        return userDTS;
    }

    @ModelAttribute("languageServiceLayer")
    public LanguageService getLanguageServiceLayer() {
        return languageService;
    }

    //------------------------------------------------------------------------------------------------------------------

    // -> ADMIN PANEL
    @UnderDevelopment
    @Deprecated
    @GetMapping("/stoody/authorized/tables/course/management")
    public String getAdminCourseOverviewPage(Model model) {
        model.addAttribute("allCourses", courseService.getAllCourses());
        return "pages/dashboard/admin-course-overview";
    }

    // -> Rest
    @GetMapping("/stoody/authorized/course/all/overview")
    @ResponseBody
    public List<CourseOverviewRequestDTO> getAllCoursesOverview(@RequestParam("limit")Long limit){
        return courseService.getAllCoursesOverview().stream().limit(limit).toList();
    }

    @PostMapping("/stoody/authorized/course/live")
    @ResponseBody
    public OutdoorResponse<?> postCourseOverviewApprove(@RequestParam("id")Long id, @RequestParam("message")String message){
        Course course = courseService.getCourseEntityById(id);
        if(course != null && course.getAuthor() != null) {
            course.setCourseStatus(CourseStatus.Live);
            notificationService.sendSimpleNotification(customUserDetailsService.getCurrentUser(),course.getAuthor(), message, SimpleNotificationBadge.SNB_AWARD);
            courseService.saveEntity(course);
            return new OutdoorResponse<>(IndoorResponse.SUCCESS, "SUCCESS");
        }
        return new OutdoorResponse<>(IndoorResponse.BAD_REQUEST,"BAD_REQUEST");
    }

    @PostMapping("/stoody/authorized/course/delete")
    @ResponseBody
    public OutdoorResponse<?> postCourseOverviewDelete(@RequestParam("id")Long id, @RequestParam("message")String message){
        Course course = courseService.getCourseEntityById(id);
        if(course != null && course.getAuthor() != null) {
            course.setCourseStatus(CourseStatus.Deleted);
            notificationService.sendSimpleNotification(customUserDetailsService.getCurrentUser(),course.getAuthor(), message, null);
            courseService.saveEntity(course);
            return new OutdoorResponse<>(IndoorResponse.SUCCESS, "SUCCESS");
        }
        return new OutdoorResponse<>(IndoorResponse.BAD_REQUEST,"BAD_REQUEST");
    }

    @PostMapping("/stoody/authorized/course/draft/feedback")
    @ResponseBody
    public OutdoorResponse<?> postCourseOverviewRejectFeedback(@RequestParam("id")Long id, @RequestParam("message")String message){
        Course course = courseService.getCourseEntityById(id);
        if(course != null && course.getAuthor() != null) {
            course.setCourseStatus(CourseStatus.Draft);
            notificationService.sendSimpleNotification(customUserDetailsService.getCurrentUser(),course.getAuthor(),message,null);
            courseService.saveEntity(course);
            return new OutdoorResponse<>(IndoorResponse.SUCCESS, "SUCCESS");
        }
        return new OutdoorResponse<>(IndoorResponse.BAD_REQUEST,"BAD_REQUEST");
    }

    @PostMapping("/stoody/authorized/course/draft")
    @ResponseBody
    public OutdoorResponse<?> postCourseOverviewReject(@RequestParam("id")Long id){
        Course course = courseService.getCourseEntityById(id);
        if(course != null && course.getAuthor() != null) {
            course.setCourseStatus(CourseStatus.Draft);
            courseService.saveEntity(course);
            return new OutdoorResponse<>(IndoorResponse.SUCCESS, "SUCCESS");
        }
        return new OutdoorResponse<>(IndoorResponse.BAD_REQUEST,"BAD_REQUEST");
    }

    //------------------------------------------------------------------------------------------------------------------

    @GetMapping("/create/new")
    public String getNewCourseRequest(){
        User user = customUserDetailsService.getCurrentUser();
        if(user == null)
            return "redirect:"+PRL.error404URL;

        Course course = courseService.getBlankCourse();
        Long courseId = courseService.saveEntity(course);
        System.out.println(courseId);

        return "redirect:/stoody/api/v1/course/stoody/course/"+courseId+"/editor";
    }

    @UnderDevelopment
    @GetMapping("/stoody/course/{id}/editor")
    public String getCourseEditorPage(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        Course course = courseService.getCourseEntityById(id);

        if (course != null) {
            if(!course.getAuthor().getUsername().equals(customUserDetailsService.getCurrentUser().getUsername()))
                return "redirect:"+PRL.error404URL;

            if(course.getCourseStatus() != null){
                if(course.getCourseStatus().equals(CourseStatus.Pending)) {
                    redirectAttributes.addAttribute("header", languageService.getContent("global.you_cannot_edit_pending_course"));
                    redirectAttributes.addAttribute("message", languageService.getContent("global.course_pending_cannot_be_edited"));
                    redirectAttributes.addAttribute("openCode", PRL.openCode);
                    return "redirect:"+PRL.redirectPageURL;
                }
            }

            CourseResponseDTO courseResponseDTO = courseService.getCourseById(id);
            model.addAttribute("courseDTO", courseResponseDTO);
            model.addAttribute("courseIdParam", String.valueOf(id));
            model.addAttribute("subCategoryElements", categoryService.getAllCategories());
            return "pages/add-course";
        }

        return "redirect:"+PRL.error404URL;
    }

    //------------------------------------------------------------------------------------------------------------------

    @GetMapping("/findAllByCategoryId/{categoryId}")
    @ResponseBody
    public OutdoorResponse<?> findAllByCategoryId(@PathVariable long categoryId) {
        return new OutdoorResponse<>(IndoorResponse.SUCCESS, courseService.getAllCoursesByCategoryId(categoryId));
    }

    @GetMapping("/findAll")
    @ResponseBody
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @GetMapping("/findCourse/{id}")
    @ResponseBody
    public OutdoorResponse<?> findCourse(@PathVariable long id) {
        return new OutdoorResponse<>(IndoorResponse.SUCCESS, courseService.getCourse(id));
    }

    @GetMapping("/findCourseWithDetails/{id}")
    @ResponseBody
    public OutdoorResponse<?> findCourseWithDetails(@PathVariable long id) {
        return new OutdoorResponse<>(IndoorResponse.SUCCESS, courseService.getCourseWithDetails(id));
    }

    @GetMapping("/findSectionByCourseId/{courseId}")
    @ResponseBody
    public OutdoorResponse<?> findSectionByCourseId(@PathVariable long courseId) {
        return new OutdoorResponse<>(IndoorResponse.SUCCESS, courseService.getSectionsByCourseId(courseId));
    }

    @GetMapping("/findLecturesBySectionId/{sectionId}")
    @ResponseBody
    public OutdoorResponse<?> findLecturesBySectionId(@PathVariable long sectionId) {
        return new OutdoorResponse<>(IndoorResponse.SUCCESS, courseService.findLecturesBySectionId(sectionId));
    }

    @PostMapping("/save")
    @ResponseBody
    public OutdoorResponse<?> saveCourse(@RequestBody CourseRequestDTO courseRequestDTO) {
        if(courseRequestDTO.getCourseStatus().equals(CourseStatus.Draft))
            return new OutdoorResponse<>(IndoorResponse.SUCCESS, courseService.save(courseRequestDTO));
        else
            return new OutdoorResponse<>(IndoorResponse.BAD_REQUEST, "Course cannot be modified");
    }

    @PostMapping("/save/submit")
    @ResponseBody
    public OutdoorResponse<?> submitCourse(@RequestBody CourseRequestDTO courseRequestDTO) {
        if(courseRequestDTO.getCourseStatus().equals(CourseStatus.Draft)) {
            courseRequestDTO.setCourseStatus(CourseStatus.Pending);
            courseService.save(courseRequestDTO);
            courseService.setCourseStatusById(courseRequestDTO.getId(),CourseStatus.Pending);
            return new OutdoorResponse<>(IndoorResponse.SUCCESS, "SUCCESS");
        }else
            return new OutdoorResponse<>(IndoorResponse.BAD_REQUEST, "Course cannot be modified");
    }

    @PostMapping("/add-comment")
    @ResponseBody
    public OutdoorResponse<?> addComment(@RequestParam(name = "courseId") long courseId,
                                         @RequestParam(name = "comment") String content,
                                         @RequestParam(name = "authorId") long authorId) {
        return new OutdoorResponse<>(IndoorResponse.SUCCESS, courseService.addComment(courseId, content, authorId));
    }

    @PostMapping("/add-rating")
    @ResponseBody
    public OutdoorResponse<?> addRating(@RequestParam(name = "courseId") long courseId,
                                         @RequestParam(name = "rating") long rating,
                                         @RequestParam(name = "authorId") long authorId) {
        return new OutdoorResponse<>(IndoorResponse.SUCCESS, courseService.addRating(courseId, rating, authorId));
    }

    @PostMapping("/purchase-course")
    @ResponseBody
    public OutdoorResponse<?> purchaseCourse(
            @RequestParam(name = "courseId") long courseId,
            @RequestParam(name = "userId") long userId) {
        return new OutdoorResponse<>(IndoorResponse.SUCCESS, courseService.purchaseCourse(courseId, userId));
    }

    @DeleteMapping("/delete")
    public OutdoorResponse<?> delete(@RequestParam long courseId) {
        courseService.delete(courseId);
        return new OutdoorResponse<>(IndoorResponse.SUCCESS, "Course deleted successfully");
    }

    @DeleteMapping("/delete-section")
    public OutdoorResponse<?> deleteSection(@RequestParam(name = "sectionId") long sectionId) {
        courseService.deleteSection(sectionId);
        return new OutdoorResponse<>(IndoorResponse.SUCCESS, "Course section deleted successfully");
    }

    @DeleteMapping("/delete-lecture")
    public OutdoorResponse<?> deleteLecture(@RequestParam(name = "lectureId") long lectureId) {
        courseService.deleteLecture(lectureId);
        return new OutdoorResponse<>(IndoorResponse.SUCCESS, "Course lecture deleted successfully");
    }
}