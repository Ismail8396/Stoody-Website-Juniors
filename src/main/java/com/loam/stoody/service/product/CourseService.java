package com.loam.stoody.service.product;

import com.loam.stoody.model.product.course.Course;
import com.loam.stoody.repository.product.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    private final CourseRepository courseRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository){
        this.courseRepository = courseRepository;
    }

    public List<Course> getAllCourses(){
        return courseRepository.findAll();
    }

    public List<Course> getAllCoursesByCategoryId(int id){
        return courseRepository.findAllByCategoryId(id);
    }
    public void addCourse(Course course){
        courseRepository.save(course);
    }

    public void removeCourseById(long id){
        courseRepository.deleteById(id);
    }

    public Optional<Course> getCourseById(long id){
        return courseRepository.findById(id);
    }
}
