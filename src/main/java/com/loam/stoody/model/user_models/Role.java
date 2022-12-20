/*
@fileName:  Role

@aka:       Role Model

@purpose:   Contains the data (that's either transient or non-transient) of a role.

@author:    OrkhanGG

@created:   01.12.2022
*/

package com.loam.stoody.model.user_models;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Entity
@Data
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(nullable = false,unique = true)
    @NotEmpty
    private String name;
}
