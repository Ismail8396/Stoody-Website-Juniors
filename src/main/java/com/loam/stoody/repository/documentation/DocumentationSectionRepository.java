package com.loam.stoody.repository.documentation;

import com.loam.stoody.model.documentation.DocumentationSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentationSectionRepository extends JpaRepository<DocumentationSection,Long> {
}
