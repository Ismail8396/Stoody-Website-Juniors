package com.loam.stoody.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name = "languages")
public class LanguageModel {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column
    private Integer id;
    @Column
    private String locale;
    @Column(name = "messagekey")
    private String key;
    @Column(name = "messagecontent")
    private String content;
}