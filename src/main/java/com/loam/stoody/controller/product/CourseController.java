package com.loam.stoody.controller.product;

import com.loam.stoody.dto.api.request.course.CourseOverviewRequestDTO;
import com.loam.stoody.dto.api.request.course.approved.ApprovedCourseDTO;
import com.loam.stoody.dto.api.request.course.pending.PendingCourseDTO;
import com.loam.stoody.dto.api.response.OutdoorResponse;
import com.loam.stoody.enums.CourseStatus;
import com.loam.stoody.enums.IndoorResponse;
import com.loam.stoody.model.product.course.pending.PendingCourse;
import com.loam.stoody.service.product.ApprovedCourseService;
import com.loam.stoody.service.product.CourseServiceBase;
import com.loam.stoody.service.product.PendingCourseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.management.Query;

@Controller
@RequestMapping("/stoody/courses")
@AllArgsConstructor
public class CourseController {
    private final CourseServiceBase courseServiceBase;
    private final ApprovedCourseService approvedCourseService;
    private final PendingCourseService pendingCourseService;

    // -> Course Overview
    @GetMapping("/overview/get/all")
    @ResponseBody
    public OutdoorResponse<?> getAllCourseOverview() {
        return new OutdoorResponse<>(IndoorResponse.SUCCESS, courseServiceBase.getAllCourseOverview());
    }

    @GetMapping("/overview/get/limited")
    @ResponseBody
    public OutdoorResponse<?> getLimitedCourseOverview(@RequestParam("limit") Long limit) {
        return new OutdoorResponse<>(IndoorResponse.SUCCESS, courseServiceBase.getLimitedCourseOverview(limit));
    }

    @PostMapping("/overview/post/status/draft")
    @ResponseBody
    public OutdoorResponse<?> postPendingCourseStatusDraft(@RequestBody CourseOverviewRequestDTO overview,
                                                           @RequestParam(value="message",required = false)String message){
        // Courses that has Live and Draft status cannot be drafted!
        if(overview.getStatus().equals(CourseStatus.Pending.toString())
                || overview.getStatus().equals(CourseStatus.Deleted.toString())){
            pendingCourseService.setStatus(overview.getId(), CourseStatus.Draft);
        }
        System.out.println(message);
        return new OutdoorResponse<>(IndoorResponse.SUCCESS, IndoorResponse.SUCCESS.toString());
    }

    @PostMapping("/overview/post/status/live")
    @ResponseBody
    public OutdoorResponse<?> postPendingCourseStatusLive(@RequestBody CourseOverviewRequestDTO overview,
                                                          @RequestParam(value="message",required = false)String message){
        if(overview.getStatus().equals(CourseStatus.Draft.toString())){
            pendingCourseService.setStatus(overview.getId(),CourseStatus.Live);

        }else if(overview.getStatus().equals(CourseStatus.Pending.toString())
                || overview.getStatus().equals(CourseStatus.Deleted.toString())){
            pendingCourseService.setStatus(overview.getId(), CourseStatus.Live);
            approvedCourseService.submitPendingCourse(pendingCourseService.getPendingCourseById(overview.getId(), PendingCourse.class));
        }
        System.out.println(message);
        return new OutdoorResponse<>(IndoorResponse.SUCCESS, IndoorResponse.SUCCESS.toString());
    }

    @PostMapping("/overview/post/status/deleted")
    @ResponseBody
    public OutdoorResponse<?> postPendingCourseStatusDeleted(@RequestBody CourseOverviewRequestDTO overview,
                                                             @RequestParam(value="message",required = false)String message){
        if(overview.getStatus().equals(CourseStatus.Draft.toString())){
            //pendingCourseService.setStatus(id, CourseStatus.Deleted);
            pendingCourseService.delete(overview.getId());
        }else if(overview.getStatus().equals(CourseStatus.Pending.toString())){
            pendingCourseService.setStatus(overview.getId(), CourseStatus.Deleted);
        }else if(overview.getStatus().equals(CourseStatus.Live.toString())){
            if(overview.getPendingCourseId() != null || overview.getApprovedCourseId() != null) {
                System.out.println(overview);
                if (overview.getPendingCourseId() == null) {
                    approvedCourseService.delete(overview.getApprovedCourseId());
                    pendingCourseService.delete(overview.getId());
                } else {
                    pendingCourseService.setStatus(overview.getPendingCourseId(), CourseStatus.Draft);
                    approvedCourseService.delete(overview.getId());
                }
            }else{
                pendingCourseService.delete(overview.getId());
            }
        }
        System.out.println(message);
        return new OutdoorResponse<>(IndoorResponse.SUCCESS, IndoorResponse.SUCCESS.toString());
    }

    // -> Approved Course

    @GetMapping("/approved/get/all")
    @ResponseBody
    public OutdoorResponse<?> getAllApprovedCourses() {
        return new OutdoorResponse<>(IndoorResponse.SUCCESS, approvedCourseService.getAllApprovedCourses(ApprovedCourseDTO.class));
    }

    @GetMapping("/approved/get/restricted/{id}")
    @ResponseBody
    public OutdoorResponse<?> getApprovedCourseByIdRestricted(@PathVariable("id")Long id){
        // TODO: return course restricted.
        return new OutdoorResponse<>(IndoorResponse.SUCCESS, approvedCourseService.getApprovedCourseById(id, ApprovedCourseDTO.class));
    }

    @PostMapping("/approved/approve/pending/{id}")
    @ResponseBody
    public OutdoorResponse<?> postApprovePendingCourseRequest(@PathVariable("id")Long id) {
        final PendingCourse pendingCourse = pendingCourseService.getPendingCourseById(id, PendingCourse.class);
        if(pendingCourse == null)
            return new OutdoorResponse<>(IndoorResponse.BAD_REQUEST, IndoorResponse.BAD_REQUEST);
        ApprovedCourseDTO approvedCourseDTO = approvedCourseService.submitPendingCourse(pendingCourse);
        if(approvedCourseDTO == null)
            return new OutdoorResponse<>(IndoorResponse.BAD_REQUEST, IndoorResponse.BAD_REQUEST);
        return new OutdoorResponse<>(IndoorResponse.SUCCESS, approvedCourseDTO);
    }

    //-> Pending Course

    // Get
    @GetMapping("/pending/get/all")
    @ResponseBody
    public OutdoorResponse<?> getAllPendingCourses() {
        return new OutdoorResponse<>(IndoorResponse.SUCCESS,pendingCourseService.getAllPendingCourses(PendingCourseDTO.class));
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

    @PostMapping("/pending/post/submit")
    @ResponseBody
    public OutdoorResponse<?> postSubmitPendingCourseRequest(@RequestBody PendingCourseDTO pendingCourseDTO){
        return new OutdoorResponse<>(IndoorResponse.SUCCESS, pendingCourseService.submitPendingCourse(pendingCourseDTO));
    }
}
