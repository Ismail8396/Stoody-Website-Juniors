package com.loam.stoody.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
public class ParentModel {
    private LocalDateTime createdDate;
    private Long createdBy;
    private LocalDateTime modifiedDate;
    private Long modifiedBy;
    private Boolean isDeleted;
}