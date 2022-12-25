/*
@fileName:  Category

@aka:       Category Model

@purpose:   Contains the data (that's either transient or non-transient) of a category.

@author:    OrkhanGG

@created:   01.12.2022
*/

package com.loam.stoody.model.product;

import lombok.Data;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "category_id")
    private int id;
    private String name;

    @OneToOne
    @JoinColumn(name = "parent_category_id")
    private Category parent;

    @OneToMany
    @JoinColumn(name = "sub_categories_category_id")
    private List<Category> subCategories;
}
