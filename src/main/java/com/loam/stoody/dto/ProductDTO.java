package com.loam.stoody.dto;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
public class ProductDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private int categoryId;

    private String ownerEmail;// I'm not sure about this. Maybe using User would be better?
    private long viewCount;
    private LocalDateTime uploadDate;

    private double price;
    private double weight;
    private String description;
    private String imageName;
}
