/*
@fileName:  LanguageModel

@aka:       Language Model

@purpose:   Contains 3 fields with non-generated values.
            "locale" field indicates the language code (Ex: en, ru, tr).
            "key" field is used by html to be replaced with message content according to the value of "locale".
            "content" field contains the actual meaning/translated text of the message key according to the value of "locale".

@example:   locale: en | key: home.welcome | content: Welcome!
            locale: tr | key: home.welcome | content: Merhaba!
            locale: ru | key: home.welcome | content: Privet!

@author:    OrkhanGG

@created:   01.12.2022
*/

package com.loam.stoody.model.i18n_models;

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