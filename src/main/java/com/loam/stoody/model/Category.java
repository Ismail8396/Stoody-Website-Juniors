package com.loam.stoody.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "category_id")
    private int id;
    private String name;

    @Column(name="icon_class_name")
    private String iconClassName;

    @OneToOne
    @JoinColumn(name = "parent_category_id")
    private Category parent;

    @OneToMany
    @JoinColumn(name = "sub_categories_category_id")
    private List<Category> subCategories;
}
