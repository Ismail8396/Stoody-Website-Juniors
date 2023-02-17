package com.loam.stoody.repository.documentation;

import com.loam.stoody.model.documentation.DocumentationCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentationCategoryRepository extends JpaRepository<DocumentationCategory, Long> {
}
