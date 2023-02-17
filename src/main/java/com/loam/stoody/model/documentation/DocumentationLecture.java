package com.loam.stoody.model.documentation;

import com.loam.stoody.model.ParentModel;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class DocumentationLecture extends ParentModel {

    @Column(nullable = false)
    private String name;

    private String description;
    @Column(nullable = false, columnDefinition = "TEXT", length = 65535)
    private String content;

    @ManyToOne(optional = false)
    @JoinColumn(name = "section_id", nullable = false)
    private DocumentationSection section;

}
