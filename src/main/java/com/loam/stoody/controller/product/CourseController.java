package com.loam.stoody.controller.product;

import com.loam.stoody.dto.api.response.OutdoorResponse;
import com.loam.stoody.global.constants.IndoorResponse;
import com.loam.stoody.model.product.course.Course;
import com.loam.stoody.service.product.CourseService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/course")
@AllArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping("/test-course")
    public String testCourse() {
        return "testing";
    }

    @GetMapping("/findAllByCategoryId/{categoryId}")
    @ResponseBody
    public OutdoorResponse findAllByCategoryId(@PathVariable long categoryId) {
        return new OutdoorResponse(IndoorResponse.SUCCESS, courseService.getAllCoursesByCategoryId(categoryId));
    }

    @GetMapping("/findAll")
    @ResponseBody
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @GetMapping("/findCourse/{id}")
    @ResponseBody
    public OutdoorResponse<?> findCourse(@PathVariable long id) {
        return new OutdoorResponse(IndoorResponse.SUCCESS, courseService.getCourse(id));
    }

    @GetMapping("/findCourseWithDetails/{id}")
    @ResponseBody
    public OutdoorResponse<?> findCourseWithDetails(@PathVariable long id) {
        return new OutdoorResponse(IndoorResponse.SUCCESS, courseService.getCourseWithDetails(id));
    }

    @GetMapping("/findSectionByCourseId/{courseId}")
    @ResponseBody
    public OutdoorResponse<?> findSectionByCourseId(@PathVariable long courseId) {
        return new OutdoorResponse(IndoorResponse.SUCCESS, courseService.getSectionsByCourseId(courseId));
    }

    @GetMapping("/findLecturesBySectionId/{sectionId}")
    @ResponseBody
    public OutdoorResponse<?> findLecturesBySectionId(@PathVariable long sectionId) {
        return new OutdoorResponse(IndoorResponse.SUCCESS, courseService.findLecturesBySectionId(sectionId));
    }

    @PostMapping("/save")
    @ResponseBody
    public OutdoorResponse<?> save(@RequestBody Course course) {
        return new OutdoorResponse(IndoorResponse.SUCCESS, courseService.save(course));
    }

    @PostMapping("/add-comment")
    @ResponseBody
    public OutdoorResponse<?> addComment(@RequestParam(name = "courseId") long courseId,
                                         @RequestParam(name = "comment") String content,
                                         @RequestParam(name = "authorId") long authorId) {
        return new OutdoorResponse(IndoorResponse.SUCCESS, courseService.addComment(courseId, content, authorId));
    }

    @PostMapping("/add-ratting")
    @ResponseBody
    public OutdoorResponse<?> addRatting(@RequestParam(name = "courseId") long courseId,
                                         @RequestParam(name = "rating") long rating,
                                         @RequestParam(name = "authorId") long authorId) {
        return new OutdoorResponse(IndoorResponse.SUCCESS, courseService.addRatting(courseId, rating, authorId));
    }

    @PostMapping("/purchase-course")
    @ResponseBody
    public OutdoorResponse<?> purchaseCourse(
            @RequestParam(name = "courseId") long courseId,
            @RequestParam(name = "userId") long userId) {
        return new OutdoorResponse(IndoorResponse.SUCCESS, courseService.purchaseCourse(courseId, userId));
    }

    @DeleteMapping("/delete")
    public OutdoorResponse<?> delete(@RequestParam long courseId) {
        courseService.delete(courseId);
        return new OutdoorResponse(IndoorResponse.SUCCESS, "Course deleted successfully");
    }

    @DeleteMapping("/delete-section")
    public OutdoorResponse<?> deleteSection(@RequestParam(name = "sectionId") long sectionId) {
        courseService.deleteSection(sectionId);
        return new OutdoorResponse(IndoorResponse.SUCCESS, "Course section deleted successfully");
    }

    @DeleteMapping("/delete-lecture")
    public OutdoorResponse<?> deleteLecture(@RequestParam(name = "lectureId") long lectureId) {
        courseService.deleteLecture(lectureId);
        return new OutdoorResponse(IndoorResponse.SUCCESS, "Course lecture deleted successfully");
    }
}