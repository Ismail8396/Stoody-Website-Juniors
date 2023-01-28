package com.loam.stoody.model.product.course;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(indexes = {
        @Index(name = "courseSectIndex_id", columnList = "id", unique = true)
})
public class CourseSection {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;

    @ManyToOne(targetEntity = Course.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    private Course course;

//    @OneToMany(
//            cascade = CascadeType.ALL,
//            targetEntity = CourseLecture.class
//    )
//    @JoinColumn(name = "course_section_id")
//    private List<CourseLecture> lectures;
}