package com.loam.stoody.model;


import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    private Category category;

    private String ownerEmail;// I'm not sure about this. Maybe using User would be better?
    private long viewCount;
    private LocalDateTime uploadDate;

    private double price;
    private double weight;
    private String description;
    private String imageName;
}
