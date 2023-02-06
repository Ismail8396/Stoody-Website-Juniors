package com.loam.stoody.service.product;

import com.loam.stoody.enums.CourseStatus;
import com.loam.stoody.model.product.course.core.CourseCore;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Constructor;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public abstract class CourseCoreService {
    final static String[] courseEntityToRequestIgnoreProps = {"promoVideo", "thumbnail", "courseCategory", "author", "approvedCourse"};
    final static String[] courseSectionEntityToRequestIgnoreProps = {"id", "pendingCourse"};
    final static String[] courseSectionRequestToEntityIgnoreProps = {"lectures"};
    final static String[] courseLectureEntityToRequestIgnoreProps = {"id", "video", "userFile", "pendingCourseSection"};
    final static String[] courseLectureRequestToEntityIgnoreProps = {"video", "userFile", "quiz"};

    public <T, R> R convertShallow(T from, Class<R> toClass) throws ReflectiveOperationException {
        Constructor<R> constructor = toClass.getDeclaredConstructor();
        R to = constructor.newInstance();
        BeanUtils.copyProperties(from, to);
        return to;
    }

    public <T, R> R convertShallow(T from, Class<R> toClass, String[] ignoreProps) throws ReflectiveOperationException {
        Constructor<R> constructor = toClass.getDeclaredConstructor();
        R to = constructor.newInstance();
        BeanUtils.copyProperties(from, to, ignoreProps);
        return to;
    }

    public <T extends CourseCore> void setIsDeleted(T course, boolean isDeleted){
        course.setIsDeleted(isDeleted);
    }

    public <T extends CourseCore> void setStatus(T course, CourseStatus status){
        course.setCourseStatus(status);
    }

    public <T extends CourseCore> LocalDateTime getCourseCreationDate(T course){
        return course.getCreatedDate();
    }

    public <T extends CourseCore> LocalDateTime getCourseModificationDate(T course){
        return course.getModifiedDate();
    }
}
