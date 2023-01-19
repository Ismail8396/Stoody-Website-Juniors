package com.loam.stoody.model.help.center;

import com.loam.stoody.model.ParentModel;
import com.loam.stoody.model.product.course.CourseCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HelpCenterArticle extends ParentModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String metaDescription = Strings.EMPTY;
    private String metaKeywords = Strings.EMPTY;
    private String metaAuthor = Strings.EMPTY;

    private String name = Strings.EMPTY;
    @Column(columnDefinition="TEXT")
    private String content = Strings.EMPTY;
    private String shortContent = Strings.EMPTY;
    private String urlKey = Strings.EMPTY;

    private Long likes = 0L;
    private Long dislikes = 0L;

    @ManyToOne(targetEntity = HelpCenterCategory.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "help_center_category_id", referencedColumnName = "id")
    private HelpCenterCategory category;
}
