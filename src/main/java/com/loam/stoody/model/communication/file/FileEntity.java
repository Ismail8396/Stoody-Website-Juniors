package com.loam.stoody.model.communication.file;

import com.loam.stoody.model.user.User;
import jakarta.persistence.*;
import lombok.Data;

@Data
@MappedSuperclass
public abstract class FileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String url;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User owner;
}
