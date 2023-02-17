package com.loam.stoody.repository.documentation;

import com.loam.stoody.model.documentation.DocumentationLecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentationLectureRepository extends JpaRepository<DocumentationLecture,Long> {
}
