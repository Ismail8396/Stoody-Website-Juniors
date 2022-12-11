package com.loam.stoody.service;

import com.loam.stoody.model.Course;
import com.loam.stoody.repository.CategoryRepository;
import com.loam.stoody.repository.CourseRepository;
import com.loam.stoody.repository.UserRepository;
import com.loam.stoody.repository.VideoRepository;
import com.loam.stoody.service.aws.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

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
