package com.loam.stoody.model.documentation;

import com.loam.stoody.model.ParentModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;


@Entity
@Data
public class DocumentationSection extends ParentModel {
    @Column(nullable = false)
    private String name;
    private String description;
    private String thumbnailUrl;

    private Long visitCount;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private DocumentationCategory category;
}
