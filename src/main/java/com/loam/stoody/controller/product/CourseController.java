package com.loam.stoody.controller.product;

import com.loam.stoody.model.product.course.Course;
import com.loam.stoody.service.product.CourseService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<?> findAllByCategoryId(@PathVariable long categoryId) {
        return ResponseEntity.ok(courseService.getAllCoursesByCategoryId(categoryId));
    }

    @GetMapping("/findAll")
    @ResponseBody
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @GetMapping("/findById/{id}")
    @ResponseBody
    public ResponseEntity<?> findById(@PathVariable long id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody Course course) {
        return ResponseEntity.ok(courseService.save(course));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam long courseId) {
        courseService.delete(courseId);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/delete-section")
    public ResponseEntity<?> deleteSection(@RequestParam(name = "sectionId") long sectionId) {
        courseService.deleteSection(sectionId);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/delete-lecture")
    public ResponseEntity<?> deleteLecture(@RequestParam(name = "lectureId") long lectureId) {
        courseService.deleteLecture(lectureId);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}