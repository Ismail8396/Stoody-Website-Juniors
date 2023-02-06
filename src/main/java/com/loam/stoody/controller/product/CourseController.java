package com.loam.stoody.controller.product;

import com.loam.stoody.dto.api.request.course.pending.PendingCourseDTO;
import com.loam.stoody.dto.api.response.OutdoorResponse;
import com.loam.stoody.enums.IndoorResponse;
import com.loam.stoody.model.product.course.approved.ApprovedCourse;
import com.loam.stoody.model.product.course.pending.PendingCourse;
import com.loam.stoody.service.product.ApprovedCourseService;
import com.loam.stoody.service.product.PendingCourseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/stoody/courses")
@AllArgsConstructor
public class CourseController {
    private final ApprovedCourseService approvedCourseService;
    private final PendingCourseService pendingCourseService;

// -> Approved Course

    // Get
    @GetMapping("/approved/get/all")
    @ResponseBody
    public OutdoorResponse<?> getAllApprovedCourses() {
        return new OutdoorResponse<>(IndoorResponse.SUCCESS, approvedCourseService.getAllApprovedCourses());
    }

    // Post
    @PostMapping("/approved/approve/pending/{id}")
    @ResponseBody
    public OutdoorResponse<?> postApprovePendingCourseRequest(@PathVariable("id")Long id) {
        final PendingCourse pendingCourse = pendingCourseService.getPendingCourseById(id, PendingCourse.class);
        if(pendingCourse == null)
            return new OutdoorResponse<>(IndoorResponse.BAD_REQUEST, IndoorResponse.BAD_REQUEST);
        ApprovedCourse approvedCourse = approvedCourseService.submitPendingCourse(pendingCourse);
        if(approvedCourse == null)
            return new OutdoorResponse<>(IndoorResponse.BAD_REQUEST, IndoorResponse.BAD_REQUEST);
        return new OutdoorResponse<>(IndoorResponse.SUCCESS, approvedCourse);
    }

//-> Pending Course

    // Get
    @GetMapping("/pending/get/all")
    @ResponseBody
    public OutdoorResponse<?> getAllPendingCourses() {
        return new OutdoorResponse<>(IndoorResponse.SUCCESS,pendingCourseService.getAllPendingCourses());
    }

    @GetMapping("/pending/get/{id}")
    @ResponseBody
    public OutdoorResponse<?> getPendingCourseById(@PathVariable("id")Long id){
        PendingCourseDTO pendingCourseDTO = pendingCourseService.getPendingCourseById(id, PendingCourseDTO.class);
        if(pendingCourseDTO == null)
            return new OutdoorResponse<>(IndoorResponse.BAD_REQUEST, null);
        return new OutdoorResponse<>(IndoorResponse.SUCCESS, pendingCourseDTO);
    }

    // Post
    @PostMapping("/pending/post/save")
    @ResponseBody
    public OutdoorResponse<?> postSavePendingCourseRequest(@RequestBody PendingCourseDTO pendingCourseDTO) {
        return new OutdoorResponse<>(IndoorResponse.SUCCESS, pendingCourseService.savePendingCourse(pendingCourseDTO));
    }
}
