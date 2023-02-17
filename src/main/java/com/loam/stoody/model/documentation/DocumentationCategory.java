package com.loam.stoody.model.documentation;

import com.loam.stoody.model.ParentModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class DocumentationCategory extends ParentModel {
    @Column(nullable = false)
    private String name;
    private String description;
}
