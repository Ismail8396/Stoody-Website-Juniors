/*
@fileName:  ParentModel

@aka:       Parent Model

@purpose:   A parent model that contains the basic fields that every model in the project should have.

@author:    aleemkhowaja

@created:   16.12.2022
*/

package com.loam.stoody.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
public class ParentModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private LocalDateTime createdDate;
    private Long createdBy;
    private LocalDateTime modifiedDate;
    private Long modifiedBy;
    private Boolean isDeleted;
}