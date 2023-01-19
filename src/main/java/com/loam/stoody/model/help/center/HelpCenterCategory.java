package com.loam.stoody.model.help.center;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class HelpCenterCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private String description;
}