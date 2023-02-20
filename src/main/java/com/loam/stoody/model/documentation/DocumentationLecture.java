package com.loam.stoody.model.documentation;

import com.loam.stoody.model.ParentModel;
import jakarta.persistence.*;
import lombok.Data;
import org.apache.logging.log4j.util.Strings;

@Entity
@Data
public class DocumentationLecture extends ParentModel {

    private String metaDescription;
    private String metaKeywords;
    private String metaAuthor;
    private String urlKey;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(optional = false)
    @JoinColumn(name = "section_id", nullable = false)
    private DocumentationSection section;

}
