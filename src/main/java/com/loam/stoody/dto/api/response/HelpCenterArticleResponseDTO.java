package com.loam.stoody.dto.api.response;

import com.loam.stoody.model.help.center.HelpCenterCategory;
import jakarta.persistence.*;
import lombok.Data;
import org.apache.logging.log4j.util.Strings;

@Data
public class HelpCenterArticleResponseDTO {
    private String metaDescription = Strings.EMPTY;
    private String metaKeywords = Strings.EMPTY;
    private String metaAuthor = Strings.EMPTY;

    private String name = Strings.EMPTY;
    private String content = Strings.EMPTY;
    private String shortContent = Strings.EMPTY;
    private String urlKey = Strings.EMPTY;

    private Integer id;// Category id
}
